package com.descartes.extensions

import org.apache.commons.validator.routines.UrlValidator
import java.net.URI
import java.net.URISyntaxException
import java.net.MalformedURLException
import java.net.URL
import java.util.logging.Logger


@Throws(URISyntaxException::class)
fun String.withoutUrlParameters(): String {
    val uri = URI(this)
    return URI(uri.scheme, uri.authority, uri.path, null, uri.fragment).toString()
}

@Throws(MalformedURLException::class)
fun String.baseUrl(): String = URL(this).let {
    "${it.protocol}://${it.host}/"
}

fun String.isValidUrl(): Boolean = UrlValidator.getInstance().isValid(this)
