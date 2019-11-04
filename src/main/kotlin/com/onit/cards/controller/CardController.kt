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
            card: CardDTO
    ): CardDTO {
        // Check if card for given ID exists. Forcing ObjectNotFoundException if not.
        card.id?.let { id -> cardService.findCard(id) ?: throw ObjectNotFoundException() }
        return CardDTO.fromCard(cardService.saveCard(card.toCard()))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Deletes this card.")
    @ApiResponses(
            ApiResponse(code = 200, message = "Card deleted"),
            ApiResponse(code = 404, message = "Card for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @DeleteMapping("/card")
    fun deleteTranslation(
            @ApiParam(value = "The ID of the card that is to be deleted", required = true)
            @PathVariable
            cardId: String
    ) {
        cardService.deleteCard(cardId)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Looks up all cards for the given game and translation.")
    @ApiResponses(
            ApiResponse(code = 200, message = "List of cards for the given translation", response = CardDTO::class, responseContainer = "List"),
            ApiResponse(code = 404, message = "Translation for given ID could not be found", response = ErrorResponseDTO::class)
    )
    @GetMapping("/cards/{translationId}")
    fun getCardsForTranslation(
            @ApiParam(value = "The ID of the translation for which the cards should be listed", required = true)
            @RequestHeader("translation-id")
            translationId: String
    ): List<CardDTO> {
        val card: List<Card> = cardService.findAllCardsForTranslation(translationId)
        return card.map { c -> CardDTO.fromCard(c) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ApiOperation(value = "Looks up all cards that match the given search string in their title.")
    @ApiResponses(
            ApiResponse(code = 200, message = "List of cards with a title like the given search string", response = CardDTO::class, responseContainer = "List")
    )
    @GetMapping("/cards/by-title")
    fun getCardsByTitle(
            @ApiParam(value = "The search string", required = true)
            @RequestParam
            searchString: String
    ): List<CardDTO> {
        val card: List<Card> = cardService.findByTitle(searchString)
        return card.map { c -> CardDTO.fromCard(c) }
    }

}