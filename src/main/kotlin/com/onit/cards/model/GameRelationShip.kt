package com.onit.cards.model

import com.onit.cards.enums.GameRelationshipType

data class GameRelationShip(val relatedGameId: String, val type: GameRelationshipType)