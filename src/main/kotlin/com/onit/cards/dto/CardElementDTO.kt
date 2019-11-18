package com.onit.cards.dto

import com.fasterxml.jackson.annotation.JsonRootName
import com.onit.cards.enums.CardElementType
import com.onit.cards.model.CardElement
import java.util.*

@JsonRootName(value = "CardElement")
data class CardElementDTO(val id: String?, val type: CardElementType, val data: String) {
    companion object CardElementDTO {
        fun fromCardElement(cardElement: CardElement) = CardElementDTO(cardElement.id, cardElement.type, cardElement.data)

    }

    fun toCardElement() = CardElement(id ?: UUID.randomUUID().toString(), type, data)
}