package com.onit.cards.service

import com.onit.cards.data.GameRepository
import com.onit.cards.model.Game
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface GameService {
    fun findGame(gameId: String): Game?
    fun findGamesByTitle(searchString: String?): List<Game>
    fun saveGame(game: Game): Game
    fun deleteGame(game: Game)
}

@Service("gameService")
class GameServiceImpl : GameService {

    val log: Logger = LoggerFactory.getLogger("GameService")

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    lateinit var gameRepository: GameRepository

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findGame(gameId: String): Game? {
        val game = gameRepository.findByIdOrNull(gameId)
        log.debug("Looked up game for id " + gameId)
        return game
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findGamesByTitle(searchString: String?): List<Game> {
        val games = searchString?.let { str -> gameRepository.findByTitleLike(str) }
                ?: gameRepository.findAll().toList()
        log.debug("Looked up " + games.size + " games for search string " + searchString)
        return games
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun saveGame(game: Game): Game {
        val game = gameRepository.save(game)
        log.info("Saved game with id " + game.id)
        return game
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun deleteGame(game: Game) {
        gameRepository.delete(game)
        log.debug("Deleted game with id " + game.id)
    }
}