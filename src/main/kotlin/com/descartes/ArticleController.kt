package com.descartes

import com.descartes.mqtp.Rabbitmq.Companion.SCRAPE_ARTICLE
import com.descartes.mqtp.Message
import com.descartes.mqtp.Publisher
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class ArticleController(val scrapeArticle: ScrapeArticle, val publisher: Publisher) {

    @PostMapping("/articles")
    suspend fun create(@RequestBody requestBody: CreateArticleRequestBody): ResponseEntity<Mono<String>> {
        val message = Message(mapOf("url" to requestBody.url))
        publisher.send(SCRAPE_ARTICLE, jacksonObjectMapper().writeValueAsString(message))
        return ok(
            CreateArticleResponseBody(text = scrapeArticle(requestBody.url)).toJson().toMono()
        )
    }
}
