package com.onit.cards.service

import com.onit.cards.data.GameRepository
import com.onit.cards.model.Game
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

    @Autowired
    lateinit var gameRepository: GameRepository

    override fun findGame(gameId: String): Game? = gameRepository.findByIdOrNull(gameId)

    override fun findGamesByTitle(searchString: String?): List<Game> =
            searchString?.let { str -> gameRepository.findByTitleLike(str) } ?: gameRepository.findAll().toList()

    override fun saveGame(game: Game): Game = gameRepository.save(game)

    override fun deleteGame(game: Game) = gameRepository.delete(game)
}