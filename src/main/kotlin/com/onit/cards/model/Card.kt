package com.onit.cards.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "cards")
@TypeAlias("Card")
data class Card(@Id val id: String, val title: String, val body: List<CardElement>, val translationId: String, val gameId: String )