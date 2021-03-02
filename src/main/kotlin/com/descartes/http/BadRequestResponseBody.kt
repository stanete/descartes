package com.descartes.http

import org.springframework.http.HttpStatus

data class BadRequestResponseBody(
    val status: Int = HttpStatus.BAD_REQUEST.value(),
    val errors: List<Error>
) : ResponseBody {

    data class Error(
        val field: String,
        val message: String
    )
}
