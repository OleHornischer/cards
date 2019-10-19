package com.onit.cards.dto

import com.onit.cards.model.Game
import java.util.*

data class GameDTO(val id: String?, val title: String, val description: String) {
    companion object GameDTO {
        fun fromGame(game: Game) = GameDTO(game.id, game.title, game.description)
    }

    fun toGame() = Game(id ?: UUID.randomUUID().toString(), title, description)
}