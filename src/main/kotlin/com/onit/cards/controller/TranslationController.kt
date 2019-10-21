package com.onit.cards.controller

import com.onit.cards.dto.TranslationDTO
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Translation
import com.onit.cards.service.TranslationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class TranslationController {
    @Autowired
    lateinit var translationService: TranslationService

    @GetMapping("/translation/{id}")
    fun getTranslation(@PathVariable id: String): TranslationDTO {
        val translation: Translation? = translationService.findTranslation(id)
        return translation?.let { t -> TranslationDTO.fromTranslation(t) } ?: throw ObjectNotFoundException()
    }

    @PutMapping("/translation")
    fun putTranslation(@RequestBody translation: TranslationDTO): TranslationDTO =
            TranslationDTO.fromTranslation(translationService.saveTranslation(translation.toTranslation()))

    @GetMapping("/translations/{gameId}")
    fun getTranslationsForGame(@PathVariable gameId: String): List<TranslationDTO> {
        val translations: List<Translation> = translationService.findAllTranslationsForGame(gameId)
        return translations.map { t -> TranslationDTO.fromTranslation(t) }
    }
}