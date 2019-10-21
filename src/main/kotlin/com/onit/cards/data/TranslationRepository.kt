package com.onit.cards.data

import com.onit.cards.model.Translation
import org.springframework.data.repository.CrudRepository

interface TranslationRepository : CrudRepository<Translation, String> {
    fun findAllByGameId(gameId: String): List<Translation>
}