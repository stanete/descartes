package com.descartes.extensions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> badRequest(body: T): ResponseEntity<T> = ResponseEntity.badRequest().body(body)

fun <T> notFound(body: T): ResponseEntity<T> = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
