package com.onit.cards.controller

import com.onit.cards.dto.ErrorResponseDTO
import com.onit.cards.dto.GameDTO
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Game
import com.onit.cards.service.DocumentService
import com.onit.cards.service.GameService
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Api(description = "The Games API")
@RestController
class GameController {

    @Autowired
    lateinit var gameService: GameService

    @Autowired
    lateinit var documentService: DocumentService

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Looks up the game with the given ID.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Game found", response = GameDTO::class),
            ApiResponse(code = 404, message = "Game for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @GetMapping("/game/{id}")
    fun getGame(
            @ApiParam(value = "The ID of the game", required = true)
            @PathVariable
            id: String
    ): GameDTO {
        val game: Game? = gameService.findGame(id)
        return game?.let { game -> GameDTO.fromGame(game) } ?: throw ObjectNotFoundException()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Downloads the pdf containing the QR codes of the cards for this game")
    @ApiResponses(
            ApiResponse(code = 200, message = "The PDF with the QR codes"),
            ApiResponse(code = 404, message = "Game for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @GetMapping("/game/qr-printout/{gameId}")
    fun getGameQrCodesPrintOut(
            request: HttpServletRequest,
            response: HttpServletResponse,
            @ApiParam(value = "The ID of the game for which to print the codes", required = true)
            @PathVariable
            gameId: String
    ) {
        val bytes = documentService.getQrCodesForGame(gameId)
        response.contentType = "application/pdf"
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"qr_codes.pdf\""));

        FileCopyUtils.copy(ByteArrayInputStream(bytes), response.outputStream)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Saves the game. If no ID is provided the game is being created as a new entry, otherwise the game with the given ID  or is updated.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Game saved", response = GameDTO::class),
            ApiResponse(code = 404, message = "Game for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @PutMapping("/game")
    fun putGame(
            @ApiParam(value = "The game to be saved. Provide null as ID if you want to create a new game", required = true)
            @RequestBody
            game: GameDTO
    ): GameDTO {
        // Check if game for given ID exists. Forcing ObjectNotFoundException if not.
        game.id?.let { id -> gameService.findGame(id)?:throw ObjectNotFoundException() }
        return GameDTO.fromGame(gameService.saveGame(game.toGame()))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Deletes this game.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Game deleted"),
            ApiResponse(code = 404, message = "Game for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @DeleteMapping("/game")
    fun deleteTranslation(
            @ApiParam(value = "The ID of the game that is to be deleted", required = true)
            @PathVariable
            gameId: String
    ) {
        gameService.deleteGame(gameId)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Searches all games that contain the given string in their name.")
    @ApiResponse(code = 200, message = "Games found", response = GameDTO::class, responseContainer = "List")
    @GetMapping("/games")
    fun getGames(
            @ApiParam(value = "The search string to be included in the title of the game. Provide null as search string or omit the parameter to list all games.", required = false)
            @RequestParam
            searchString: String?
    ): List<GameDTO> {
        val games: List<Game> = gameService.findGamesByTitle(searchString)
        return games.map { game -> GameDTO.fromGame(game) }
    }
}