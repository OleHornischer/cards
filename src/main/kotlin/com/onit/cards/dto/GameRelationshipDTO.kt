package com.onit.cards.dto

import com.fasterxml.jackson.annotation.JsonRootName
import com.onit.cards.enums.GameRelationshipType
import com.onit.cards.model.Game
import com.onit.cards.model.GameRelationShip
import java.util.*

@JsonRootName(value = "GameRelationship")
data class GameRelationshipDTO(val relatedGameId: String, val type: GameRelationshipType) {
    companion object GameRelationshipDTO {
        fun fromGameRelationship(gameRelationShip: GameRelationShip) = GameRelationshipDTO(gameRelationShip.relatedGameId, gameRelationShip.type)
    }

    fun toGameRelationship() = GameRelationShip(relatedGameId, type)
}