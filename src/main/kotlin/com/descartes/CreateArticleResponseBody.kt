package com.descartes

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class CreateArticleResponseBody(
    val text: String,
) {
    fun toJson(): String = jacksonObjectMapper().writeValueAsString(this)
}
