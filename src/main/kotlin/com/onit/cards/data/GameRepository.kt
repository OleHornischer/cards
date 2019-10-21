package com.onit.cards.data

import com.onit.cards.model.Game
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface GameRepository : CrudRepository<Game, String>{
    fun findByTitleLike(@Param("title") title: String): List<Game>
}