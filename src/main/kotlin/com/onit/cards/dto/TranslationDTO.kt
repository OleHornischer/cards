package com.onit.cards.dto

import com.onit.cards.model.Translation
import java.util.*

data class TranslationDTO(val id: String?, val language: String, val name: String, val gameId: String, val sessionCount: Int) {
    companion object TranslationDTO {
        fun fromTranslation(translation: Translation) = TranslationDTO(translation.id, translation.language, translation.name, translation.gameId, translation.sessionCount)
    }

    fun toTranslation() = Translation(id ?: UUID.randomUUID().toString(), language, name, gameId, sessionCount)
}