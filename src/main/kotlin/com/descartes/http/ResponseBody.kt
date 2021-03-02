package com.descartes.http

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

interface ResponseBody {
    fun toJson(): String = jacksonObjectMapper().writeValueAsString(this)
}
