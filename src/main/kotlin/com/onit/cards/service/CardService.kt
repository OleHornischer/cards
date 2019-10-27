package com.onit.cards.service

import com.onit.cards.data.CardRepository
import com.onit.cards.model.Card
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface CardService {
    fun findCard(cardId: String): Card?
    fun findAllCardsForGameAndTranslation(gameId: String, translationId: String): List<Card>
    fun findByTitle(searchString: String): List<Card>
    fun saveCard(card: Card): Card
    fun deleteCard(card: Card)
}

@Service("cardService")
class CardServiceImpl : CardService {

    val log: Logger = LoggerFactory.getLogger("CardService")

    @Autowired
    lateinit var cardRepository: CardRepository

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findCard(cardId: String): Card? {
        val card = cardRepository.findByIdOrNull(cardId)
        log.debug("Looked up card for id " + cardId)
        return card
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findAllCardsForGameAndTranslation(gameId: String, translationId: String): List<Card> {
        val cards = cardRepository.findAllByGameIdAndTranslationId(gameId, translationId)
        log.debug("Looked up "+cards.size+" cards for gameId " + gameId + " and translationId " + translationId)
        return cards
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findByTitle(searchString: String): List<Card> {
        val cards = cardRepository.findByTitleLike(searchString)
        log.debug("Looked up "+cards.size+" cards for searchString " + searchString)
        return cards
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun saveCard(card: Card): Card {
        val card = cardRepository.save(card)
        log.info("Saved card for id " + card.id)
        return card
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun deleteCard(card: Card) {
        cardRepository.delete(card)
        log.debug("Deleted card with id " + card.id)
    }
}