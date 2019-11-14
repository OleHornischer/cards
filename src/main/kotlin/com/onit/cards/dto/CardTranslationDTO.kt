package com.onit.cards.dto

import com.fasterxml.jackson.annotation.JsonRootName
import com.onit.cards.model.CardTranslation

@JsonRootName(value = "CardTranslation")
data class CardTranslationDTO(val language:String, val body: List<CardElementDTO>) {
    companion object CardTranslationDTO {
        fun fromCardTranslation(cardTranslation: CardTranslation) =
                CardTranslationDTO(cardTranslation.language, cardTranslation.body.map { elem -> CardElementDTO.fromCardElement(elem) })

    }

    fun toCardTranslation() = CardTranslation(language, body.map(CardElementDTO::toCardElement))
}