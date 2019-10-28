package com.onit.cards.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "games")
@TypeAlias("Game")
data class Game(@Id val id: String, val title: String, val description: String, val vendor: String, val image: String?, val relatedGames: List<GameRelationShip>) {
    @Deprecated("Used just for compatibility of old data")
    constructor(id: String, title: String, description: String, vendor: String, image: String?) : this(id, title, description, vendor, image, ArrayList())
}