package com.onit.cards.dto

import com.fasterxml.jackson.annotation.JsonRootName
import com.onit.cards.model.Card
import java.util.*

@JsonRootName(value = "Card")
data class CardDTO(val id: String?, val title: String, val translation: List<CardTranslationDTO>, val gameId: String) {

    companion object CardDTO {
        fun fromCard(card: Card ) =
                CardDTO(card.id, card.title, card.translations.map { translation -> CardTranslationDTO.fromCardTranslation(translation) }, card.gameId )
    }

    fun toCard() =
        Card(id ?: UUID.randomUUID().toString(), title, translation.map { elem -> elem.toCardTranslation() }, gameId)
}