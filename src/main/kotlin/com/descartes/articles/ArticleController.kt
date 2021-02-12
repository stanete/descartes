package com.descartes.articles

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
class ArticleController(val presenter: ArticlePresenter) {

    @PostMapping("/articles")
    suspend fun create(@RequestBody requestBody: CreateArticleRequestBody): ResponseEntity<Mono<String>> {
        val article = presenter.create(requestBody.url)
        return ok(
            CreateArticleResponseBody(url = article.url).toJson().toMono()
        )
    }
}
