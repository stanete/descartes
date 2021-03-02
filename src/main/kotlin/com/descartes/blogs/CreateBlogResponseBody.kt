package com.descartes.blogs

import com.descartes.http.ResponseBody

data class CreateBlogResponseBody(
    val url: String
) : ResponseBody
