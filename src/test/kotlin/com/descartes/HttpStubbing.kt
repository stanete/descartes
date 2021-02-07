package com.descartes

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import wiremock.org.apache.http.HttpStatus
import wiremock.org.eclipse.jetty.http.HttpMethod
import org.junit.jupiter.api.fail
import org.springframework.core.io.ClassPathResource
import java.io.InputStreamReader

open class HttpStub(
    val url: String,
    val responseBody: String,
    val responseCode: Int = HttpStatus.SC_OK,
    val httpMethod: HttpMethod = HttpMethod.GET,
    val httpHeaders: HttpHeaders = HttpHeaders(HttpHeader.httpHeader("Content-Type", "application/json")),
)

class ArticleHttpStub(
    url: String,
    responseBody: String,
    responseCode: Int = HttpStatus.SC_OK,
) : HttpStub(
    url,
    responseBody,
    responseCode,
    httpMethod = HttpMethod.GET,
    httpHeaders = HttpHeaders(HttpHeader.httpHeader("Content-Type", " text/html; charset=utf-8"))
)

fun stub(httpStub: HttpStub) = stub(listOf(httpStub))

fun stub(httpStubs: List<HttpStub>) = httpStubs.forEach {
    WireMock.stubFor(
        when (it.httpMethod) {
            HttpMethod.GET -> WireMock.get(WireMock.urlEqualTo(it.url))
            HttpMethod.POST -> WireMock.post(WireMock.urlEqualTo(it.url))
            else -> throw fail { "Request for ${it.httpMethod} HTTP method are not configured." }
        }.willReturn(
            WireMock.aResponse()
                .withStatus(it.responseCode)
                .withHeaders(it.httpHeaders)
                .withBody(it.responseBody)
        )
    )
}

fun getStubContent(resourcePath: String): String = InputStreamReader(ClassPathResource(resourcePath).inputStream)
    .readLines()
    .joinToString(separator = "") {
        it.trim()
    }
