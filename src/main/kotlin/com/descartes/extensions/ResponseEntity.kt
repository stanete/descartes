package com.descartes.extensions

import org.springframework.http.ResponseEntity

fun <T> badRequest(body: T): ResponseEntity<T>? {
    return ResponseEntity.badRequest().body(body)
}
