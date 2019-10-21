package com.onit.cards.controller

import com.onit.cards.dto.CardDTO
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Card
import com.onit.cards.service.CardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class CardController {

    @Autowired
    lateinit var cardService: CardService

    @GetMapping("/card/{id}")
    fun getCard(@PathVariable id: String): CardDTO {
        val card: Card? = cardService.findCard(id)
        return card?.let { c -> CardDTO.fromCard(c) } ?: throw ObjectNotFoundException()
    }

    @GetMapping("/cards/{gameId}")
    fun getCardsForGameAndTranslation(@PathVariable gameId: String, @RequestHeader("translation-id") translationId: String): List<CardDTO> {
        val card: List<Card> = cardService.findAllCardsForGameAndTranslation(gameId, translationId)
        return card.map { c -> CardDTO.fromCard(c) }
    }

    @PutMapping("/card")
    fun putCard(@RequestBody card: CardDTO): CardDTO =
            CardDTO.fromCard(cardService.saveCard(card.toCard()))

}