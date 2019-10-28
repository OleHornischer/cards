package com.onit.cards.dto

import com.onit.cards.enums.GameRelationshipType
import com.onit.cards.model.Game
import com.onit.cards.model.GameRelationShip
import java.util.*

data class GameRelationshipDTO(val relatedGameId: String, val type: GameRelationshipType) {
    companion object GameRelationshipDTO {
        fun fromGameRelationship(gameRelationShip: GameRelationShip) = GameRelationshipDTO(gameRelationShip.relatedGameId, gameRelationShip.type)
    }

    fun toGameRelationship() = GameRelationShip(relatedGameId, type)
}