package com.onit.cards.controller

import com.onit.cards.dto.GameDTO
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Game
import com.onit.cards.service.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class GameController {

    @Autowired
    lateinit var gameService: GameService

    @GetMapping("/game/{id}")
    fun getGame(@PathVariable id: String): GameDTO {
        val game: Game? = gameService.findGame(id)
        return game?.let { game -> GameDTO.fromGame(game) } ?: throw ObjectNotFoundException()
    }

    @PutMapping("/game")
    fun putGame(@RequestBody game: GameDTO): GameDTO =
            GameDTO.fromGame(gameService.saveGame(game.toGame()))

    @GetMapping("/games")
    fun getGames(@RequestParam searchString: String?): List<GameDTO> {
        val games: List<Game> = gameService.findGamesByTitle(searchString)
        return games.map { game -> GameDTO.fromGame(game) }
    }
}