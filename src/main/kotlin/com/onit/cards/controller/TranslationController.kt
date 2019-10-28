package com.onit.cards.controller

import com.onit.cards.dto.ErrorResponseDTO
import com.onit.cards.dto.TranslationDTO
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Translation
import com.onit.cards.service.DocumentService
import com.onit.cards.service.TranslationService
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Api(description = "The Translations API")
@RestController
class TranslationController {

    @Autowired
    lateinit var translationService: TranslationService

    @Autowired
    lateinit var documentService: DocumentService

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Looks up the translation with the given ID.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Translation found", response = TranslationDTO::class),
            ApiResponse(code = 404, message = "Translation for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @GetMapping("/translation/{id}")
    fun getTranslation(
            @ApiParam(value = "The ID of the translation", required = true)
            @PathVariable
            id: String
    ): TranslationDTO {
        val translation: Translation? = translationService.findTranslation(id)
        return translation?.let { t -> TranslationDTO.fromTranslation(t) } ?: throw ObjectNotFoundException()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Saves the translation. If no ID is provided the translation is being created as a new entry, otherwise the translation with the given ID  or is updated.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Translation saved", response = TranslationDTO::class),
            ApiResponse(code = 404, message = "Translation for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @PutMapping("/translation")
    fun putTranslation(
            @ApiParam(value = "The translation to be saved. Provide null as ID if you want to create a new translation", required = true)
            @RequestBody
            translation: TranslationDTO
    ): TranslationDTO {
        // Check if translation for given ID exists. Forcing ObjectNotFoundException if not.
        translation.id?.let { id -> translationService.findTranslation(id) ?: throw ObjectNotFoundException() }
        return TranslationDTO.fromTranslation(translationService.saveTranslation(translation.toTranslation()))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Starts a new session for this translation.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Session started"),
            ApiResponse(code = 404, message = "Translation for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @PostMapping("/translation")
    fun postTranslation(
            @ApiParam(value = "The translation ID for which a session is to be started", required = true)
            @PathVariable
            translationId: String
    ) {
        translationService.startSession(translationId)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Finds the translations for a given game.")
    @ApiResponses(
            ApiResponse(code = 200, message = "The translations of the game"),
            ApiResponse(code = 404, message = "The game was not found.", response = ErrorResponseDTO::class)
    )
    @GetMapping("/translations/{gameId}")
    fun getTranslationsForGame(
            @ApiParam(value = "The ID of the game for which to list the translations", required = true)
            @PathVariable
            gameId: String
    ): List<TranslationDTO> {
        val translations: List<Translation> = translationService.findAllTranslationsForGame(gameId)
        return translations.map { t -> TranslationDTO.fromTranslation(t) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Downloads the pdf containing the QR codes of the cards in this translation")
    @ApiResponses(
            ApiResponse(code = 200, message = "The PDF with the QR codes"),
            ApiResponse(code = 404, message = "Translation for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @GetMapping("/translation/qr-printout/{translationId}")
    fun getTranslationPrintOut(
            request: HttpServletRequest,
            response: HttpServletResponse,
            @ApiParam(value = "The ID of the translation for which to print the codes", required = true)
            @PathVariable
            translationId: String
    ) {
        val bytes = documentService.getQrCodesForTranslation(translationId)
        response.contentType = "application/pdf"
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"qr_codes.pdf\""));

        FileCopyUtils.copy(ByteArrayInputStream(bytes), response.getOutputStream())
    }
}