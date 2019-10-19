package com.onit.cards.data

import com.onit.cards.model.Game
import org.springframework.data.repository.CrudRepository

interface GameRepository : CrudRepository<Game, String>