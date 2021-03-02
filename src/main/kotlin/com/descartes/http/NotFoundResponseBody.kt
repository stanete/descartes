package com.descartes.http

import org.springframework.http.HttpStatus

data class NotFoundResponseBody(
    val status: Int = HttpStatus.NOT_FOUND.value(),
    val errors: List<Error>
) : ResponseBody {

    data class Error(
        val field: String,
        val message: String
    )
}
