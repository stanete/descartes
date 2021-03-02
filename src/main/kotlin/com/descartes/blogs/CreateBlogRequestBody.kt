package com.descartes.blogs

import com.descartes.http.BadRequestResponseBody.Error
import com.descartes.extensions.isValidUrl
import com.descartes.http.BadRequestResponseBody
import com.descartes.http.RequestBody
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

data class CreateBlogRequestBody(
    val url: String,
) : RequestBody {
    override fun validate(): Result<Unit, List<Error>> = when {
        url.isEmpty() -> Err(
            listOf(
                Error(
                    field = "url",
                    message = "Url is empty."
                )
            )
        )
        url.isValidUrl().not() -> Err(
            listOf(
                Error(
                    field = "url",
                    message = "Url is not valid."
                )
            )
        )
        else -> Ok(Unit)
    }
}
