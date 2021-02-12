package com.descartes.articles

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class CreateArticleResponseBody(
    val url: String,
) {
    fun toJson(): String = jacksonObjectMapper().writeValueAsString(this)
}
