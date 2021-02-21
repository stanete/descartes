package com.descartes.mqtp

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class Message(
    val data: Map<String, String> = emptyMap(),
) {
    fun toJson(): String = jacksonObjectMapper().writeValueAsString(this)
}
