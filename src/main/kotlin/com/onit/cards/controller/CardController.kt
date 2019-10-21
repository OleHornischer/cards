package com.onit.cards.controller

import com.onit.cards.dto.CardDTO
import com.onit.cards.dto.ErrorResponseDTO
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Card
import com.onit.cards.service.CardService
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Api(description = "The Cards API")
@RestController
class CardController {

    @Autowired
    lateinit var cardService: CardService

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Looks up the card with the given ID.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Card found", response = CardDTO::class),
            ApiResponse(code = 404, message = "Card for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @GetMapping("/card/{id}")
    fun getCard(
            @ApiParam(value = "The ID of the card", required = true)
            @PathVariable
            id: String
    ): CardDTO {
        val card: Card? = cardService.findCard(id)
        return card?.let { c -> CardDTO.fromCard(c) } ?: throw ObjectNotFoundException()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Saves the card. If no ID is provided the card is being created as a new entry, otherwise the card with the given ID  or is updated.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Card saved", response = CardDTO::class),
            ApiResponse(code = 404, message = "Card for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @PutMapping("/card")
    fun putCard(
            @ApiParam(value = "The card to be saved. Provide null as ID if you want to create a new card", required = true)
            @RequestBody
            card: CardDTO): CardDTO {
        // Check if card for given ID exists. Forcing ObjectNotFoundException if not.
        card.id?.let { id -> cardService.findCard(id) }
        return CardDTO.fromCard(cardService.saveCard(card.toCard()))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Looks up all cards for the given game and translation.")
    @ApiResponses(
            ApiResponse(code = 200, message = "List of cards for the given game and translation", response = CardDTO::class, responseContainer = "List"),
            ApiResponse(code = 404, message = "Game or translation for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @GetMapping("/cards/{gameId}")
    fun getCardsForGameAndTranslation(
            @ApiParam(value = "The ID of the game for which the cards should be listed", required = true)
            @PathVariable
            gameId: String,
            @ApiParam(value = "The ID of the translation for which the cards should be listed", required = true)
            @RequestHeader("translation-id")
            translationId: String): List<CardDTO> {
        val card: List<Card> = cardService.findAllCardsForGameAndTranslation(gameId, translationId)
        return card.map { c -> CardDTO.fromCard(c) }
    }

}