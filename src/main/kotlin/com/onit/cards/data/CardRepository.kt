package com.onit.cards.data

import com.onit.cards.model.Card
import org.springframework.data.repository.CrudRepository

interface CardRepository : CrudRepository<Card, String> {
    fun findAllByGameId(gameId: String): List<Card>
}