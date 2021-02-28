package com.descartes.http

import com.github.michaelbull.result.Result

interface RequestBody {
    fun validate(): Result<Unit, BadRequestResponseBody>
}
