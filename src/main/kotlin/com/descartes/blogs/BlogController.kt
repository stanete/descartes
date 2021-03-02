package com.descartes.blogs

import com.descartes.extensions.badRequest
import com.descartes.http.BadRequestResponseBody
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class BlogController(private val presenter: BlogPresenter) {

    @PostMapping("/blogs")
    suspend fun create(
        @RequestBody requestBody: CreateBlogRequestBody
    ): ResponseEntity<Mono<String>> = when (val it = requestBody.validate()) {
        is Ok -> {
            val blog = presenter.create(requestBody.url)
            ok(
                CreateBlogResponseBody(url = blog.url).toJson().toMono()
            )
        }
        is Err -> badRequest(
            BadRequestResponseBody(errors = it.error).toJson().toMono()
        )
    }
}
