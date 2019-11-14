package com.onit.cards.dto

import com.fasterxml.jackson.annotation.JsonRootName
import com.onit.cards.enums.CardElementType
import com.onit.cards.model.CardElement

@JsonRootName(value = "CardElement")
data class CardElementDTO(val type: CardElementType, val data: String) {
    companion object CardElementDTO {
        fun fromCardElement(cardElement: CardElement) = CardElementDTO(cardElement.type, cardElement.data)

    }

    fun toCardElement() = CardElement(type, data)
}