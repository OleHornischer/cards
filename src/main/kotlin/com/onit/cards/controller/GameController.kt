package com.onit.cards.controller

import com.onit.cards.dto.CardDTO
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Card
import org.springframework.web.bind.annotation.*

@RestController
class GameController {
    @GetMapping("/game/{id}")
    fun getGame(@PathVariable id: String) : GameDTO {
        val card : Card? = cardService.findCard(id)
        return card?.let(cardMapper::toDTO) ?: throw ObjectNotFoundException()
    }

    @PutMapping("/game")
    fun putCard(@RequestBody game: GameDTO): GameDTO =
            cardMapper.toDTO(cardService.saveCard(cardMapper.fromDTO(card)))
}