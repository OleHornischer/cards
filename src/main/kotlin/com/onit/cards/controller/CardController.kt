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
    fun getCard(@PathVariable id: String) : CardDTO {
        val card : Card? = cardService.findCard(id)
        return card?.toDTO() ?: throw ObjectNotFoundException()
    }

    @PutMapping("/card")
    fun putCard(@RequestBody card: CardDTO): CardDTO =
            cardMapper.toDTO(cardService.saveCard(cardMapper.fromDTO(card)))

}