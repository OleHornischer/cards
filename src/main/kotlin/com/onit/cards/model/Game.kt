package com.onit.cards.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "games")
@TypeAlias("Game")
data class Game(@Id val id: String, val title: String, val description: String, val vendor: String, val image: String?, val relatedGames:List<GameRelationShip>)