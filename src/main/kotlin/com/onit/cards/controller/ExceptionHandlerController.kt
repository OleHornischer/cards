package com.onit.cards.controller

import com.onit.cards.dto.ErrorResponseDTO
import com.onit.cards.exception.ObjectNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class ExceptionHandlerController {

    @ExceptionHandler(ObjectNotFoundException::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleObjectNotFound(exception: ObjectNotFoundException): ErrorResponseDTO {
        return ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), exception.message ?: HttpStatus.NOT_FOUND.reasonPhrase)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNoHandlerFound(): ErrorResponseDTO {
        return ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), "No handler found for request.")
    }
}