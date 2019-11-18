package com.onit.cards.service

import com.onit.cards.data.CardRepository
import com.onit.cards.enums.CardElementType
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Card
import com.onit.cards.model.CardElement
import com.onit.cards.model.CardTranslation
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

    @Autowired
    lateinit var fileStorageServiceFactory: FileStorageServiceFactory

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findCard(cardId: String, language: String?): Card? {
        val card = cardRepository.findByIdOrNull(cardId)
        log.debug("Looked up card for id " + cardId)
        return card?.let { c ->
            Card(c.id, c.title, loadPictures(c.translations.filter { translation ->
                language?.equals(translation.language) ?: true
            }), c.gameId)
        }
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
        val saved = cardRepository.save(Card(card.id, card.title, savePictures(card.translations), card.gameId))
        log.info("Saved card for id " + saved.id)
        return saved
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun deleteCard(cardId: String) {
        val card = cardRepository.findByIdOrNull(cardId)
        card?.let { c -> {
            deletePictures(c.translations)
            cardRepository.delete(c)
        } } ?: throw ObjectNotFoundException()
        log.debug("Deleted card with id " + card.id)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun loadPictures(cardTranslations: List<CardTranslation>): List<CardTranslation> =
            cardTranslations.map { t ->
                CardTranslation(t.language,
                        t.body.map { elem ->
                            if (CardElementType.IMAGE == elem.type)
                                CardElement(elem.id, elem.type, fileStorageServiceFactory.getFileStorageService().loadFile(elem.id))
                            else
                                elem
                        })
            }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun savePictures(cardTranslations: List<CardTranslation>): List<CardTranslation> {
        cardTranslations.forEach { t ->
            t.body.filter { elem -> CardElementType.IMAGE == elem.type }
                    .forEach { elem -> fileStorageServiceFactory.getFileStorageService().storeFile(elem.id, elem.data) }
        }

        return cardTranslations.map { t ->
            CardTranslation(t.language,
                    t.body.map { elem ->
                        if (CardElementType.IMAGE == elem.type) CardElement(elem.id, elem.type, elem.id) else elem
                    })
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun deletePictures(cardTranslations: List<CardTranslation>) {
        cardTranslations.forEach { t ->
            t.body.filter { elem -> CardElementType.IMAGE == elem.type }
                    .forEach { elem -> fileStorageServiceFactory.getFileStorageService().deleteFile(elem.id) }
        }
    }
}