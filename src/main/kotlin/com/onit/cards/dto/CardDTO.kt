package com.onit.cards.dto

import com.onit.cards.model.Card
import java.util.*

data class CardDTO(val id: String?, val title: String, val body: List<CardElementDTO>, val translationId: String, val gameId: String) {

    companion object CardDTO {
        fun fromCard(card: Card) = CardDTO(card.id, card.title, card.body.map { elem -> CardElementDTO.fromCardElement(elem) }, card.translationId, card.gameId)
    }

    fun toCard() = Card(id ?: UUID.randomUUID().toString(), title, body.map { elem -> elem.toCardElement() }, translationId, gameId)
}