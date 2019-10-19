package com.onit.cards.service

import com.onit.cards.data.CardRepository
import com.onit.cards.model.Card
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface CardService {
    fun findCard(cardId: String): Card?
    fun findAllCardsForGame(gameId: String): List<Card>
    fun saveCard(card: Card): Card
}

@Service("cardService")
class CardServiceImpl : CardService {

    @Autowired
    lateinit var cardRepository: CardRepository

    override fun findCard(cardId: String): Card? = cardRepository.findByIdOrNull(cardId)

    override fun findAllCardsForGame(gameId: String): List<Card> = cardRepository.findAllByGameId(gameId)

    override fun saveCard(card: Card): Card = cardRepository.save(card)

}