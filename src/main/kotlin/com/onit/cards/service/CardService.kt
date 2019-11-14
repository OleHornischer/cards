package com.onit.cards.service

import com.onit.cards.data.CardRepository
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Card
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface CardService {
    fun findCard(cardId: String, language: String? = null): Card?
    fun findAllCardsForGame(gameId: String): List<Card>
    fun findByTitle(searchString: String): List<Card>
    fun saveCard(card: Card): Card
    fun deleteCard(cardId: String)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Service("cardService")
class CardServiceImpl : CardService {

    val log: Logger = LoggerFactory.getLogger("CardService")

    @Autowired
    lateinit var cardRepository: CardRepository

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findCard(cardId: String, language: String?): Card? {
        val card = cardRepository.findByIdOrNull(cardId)
        log.debug("Looked up card for id " + cardId)
        return card?.let { c -> Card(c.id, c.title, c.translations.filter { translation -> language?.equals(translation.language)?:true }, c.gameId) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findAllCardsForGame(gameId: String): List<Card> {
        val cards = cardRepository.findAllByGameId(gameId)
        log.debug("Looked up " + cards.size + " cards for gameId " + gameId)
        return cards
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findByTitle(searchString: String): List<Card> {
        val cards = cardRepository.findByTitleLike(searchString)
        log.debug("Looked up " + cards.size + " cards for searchString " + searchString)
        return cards
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun saveCard(card: Card): Card {
        val card = cardRepository.save(card)
        log.info("Saved card for id " + card.id)
        return card
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun deleteCard(cardId: String) {
        val card = cardRepository.findByIdOrNull(cardId)
        card?.let { c -> cardRepository.delete(c) } ?: throw ObjectNotFoundException()
        log.debug("Deleted card with id " + card.id)
    }
}