package com.descartes.extensions

import java.net.URI
import java.net.URISyntaxException
import java.net.MalformedURLException
import java.net.URL


@Throws(URISyntaxException::class)
fun String.withoutUrlParameters(): String {
    val uri = URI(this)
    return URI(uri.scheme, uri.authority, uri.path, null, uri.fragment).toString()
}

fun String.isValidUrl(): Boolean = try {
    URL(this).toURI()
    true
} catch (e: MalformedURLException) {
    false
} catch (e: URISyntaxException) {
    false
}
