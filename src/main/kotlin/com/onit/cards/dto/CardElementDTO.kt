package com.onit.cards.dto

import com.onit.cards.enums.CardElementType
import com.onit.cards.model.CardElement

data class CardElementDTO(val type: CardElementType, val data: String) {
    companion object CardElementDTO {
        fun fromCardElement(cardElement: CardElement) = CardElementDTO(cardElement.type, cardElement.data)

    }

    fun toCardElement() = CardElement(type, data)
}