package com.descartes.http

import com.descartes.http.BadRequestResponseBody.Error
import com.github.michaelbull.result.Result

interface RequestBody {
    fun validate(): Result<Unit, List<Error>>
}
