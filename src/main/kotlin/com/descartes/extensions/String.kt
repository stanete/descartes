package com.descartes.extensions

import java.net.URI
import java.net.URISyntaxException

@Throws(URISyntaxException::class)
fun String.withoutUrlParameters(): String {
    val uri = URI(this)
    return URI(uri.scheme, uri.authority, uri.path, null, uri.fragment).toString()
}
