package com.onit.cards.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "translations")
@TypeAlias("Translation")
data class Translation(@Id val id: String, val language: String, val name: String, val gameId: String, val sessionCount: Int)
