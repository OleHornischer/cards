package com.onit.cards

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
//@ComponentScan(basePackages = arrayOf("com.onit.cards"))
class CardsApplication

fun main(args: Array<String>) {
	runApplication<CardsApplication>(*args)
}
