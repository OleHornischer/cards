package com.onit.cards.dto

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName(value = "Error")
data class ErrorResponseDTO(val errorCode : Int, val errorMessage: String)