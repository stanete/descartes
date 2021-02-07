package com.descartes

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
class ArticleController(val scrapeArticle: ScrapeArticle) {

    @PostMapping("/articles")
    suspend fun create(@RequestBody requestBody: CreateArticleRequestBody): ResponseEntity<Mono<String>> = ok(
        CreateArticleResponseBody(text = scrapeArticle(requestBody.url)).toJson().toMono()
    )
}
