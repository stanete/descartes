package com.descartes.articles

import com.descartes.http.ResponseBody

data class CreateArticleResponseBody(
    val url: String,
) : ResponseBody
