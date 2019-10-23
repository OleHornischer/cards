package com.onit.cards.controller

import com.onit.cards.dto.ErrorResponseDTO
import com.onit.cards.exception.ObjectNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class ExceptionHandlerController {

    val log: Logger = LoggerFactory.getLogger("ExceptionHandlerController")

    @ExceptionHandler(ObjectNotFoundException::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleObjectNotFound(exception: ObjectNotFoundException): ErrorResponseDTO {
        log.error("404 - An object was not found: " + exception.message, exception)
        return ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), exception.message ?: HttpStatus.NOT_FOUND.reasonPhrase)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNoHandlerFound(exception: NoHandlerFoundException): ErrorResponseDTO {
        log.error("400 - A matching header could not be determined: " + exception.message, exception)
        return ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), "No handler found for request.")
    }
}