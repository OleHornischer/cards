package com.onit.cards.data

import com.onit.cards.model.Card
import com.onit.cards.model.Game
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface CardRepository : CrudRepository<Card, String> {

    fun findAllByGameIdAndTranslationId(gameId: String, translationId: String): List<Card>

    fun findByTitleLike(@Param("title") title: String): List<Card>

}