package com.onit.cards.dto

import com.onit.cards.model.Game
import com.onit.cards.model.GameRelationShip
import java.util.*
import kotlin.collections.ArrayList

data class GameDTO(val id: String?, val title: String, val description: String, val vendor: String, val image: String?, val relatedGames: List<GameRelationshipDTO>?) {
    companion object GameDTO {
        fun fromGame(game: Game) = GameDTO(game.id,
                game.title,
                game.description,
                game.vendor,
                game.image,
                game.relatedGames.map { r -> GameRelationshipDTO.fromGameRelationship(r) })
    }

    fun toGame() = Game(id ?: UUID.randomUUID().toString(),
            title,
            description,
            vendor,
            image,
            relatedGames?.map { r -> GameRelationShip(r.relatedGameId, r.type) }?:ArrayList())
}