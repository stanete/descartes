package com.descartes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DescartesApplication

fun main(args: Array<String>) {
	runApplication<DescartesApplication>(*args)
}
