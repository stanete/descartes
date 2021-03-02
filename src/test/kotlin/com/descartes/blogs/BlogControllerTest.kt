package com.descartes.blogs

import com.descartes.ApiContractAssertion.Companion.assertResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.util.logging.Logger

@SpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension::class)
@AutoConfigureWireMock(port = 8089)
class BlogControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var blogRepository: BlogRepository

    @AfterEach
    fun setUp() {
        /**
         * Automatic rollback with @Transaction annotation is not supported when applied directly from
         * the "web layer". Using either RANDOM_PORT or DEFINED_PORT implicitly provides a real servlet environment.
         * HTTP client and server will run in separate threads, thus separate transactions. Any transaction initiated
         * on the server won’t rollback in this case. So the database needs to be cleaned manually.
         */
        blogRepository.deleteAll()
    }

    @Test
    fun `When creating a new blog with a url returns a response with the created blog`() {
        val url = "https://stanete.com/"
        val body = CreateBlogRequestBody(url = url)

        val response = webTestClient.post()
            .uri("/blogs")
            .body(Mono.just(body), CreateBlogRequestBody::class.java)
            .header("Content-Type", "application/json")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody!!

        blogRepository.findAll().size shouldBeEqualTo 1
        blogRepository.findById(url).get() shouldBeEqualTo Blog(url)
        assertResponse(response).matchesJsonFile("create_blog_response.json")
    }

    @Test
    fun `When creating a new blog with an empty url returns a response with error`() {
        val body = CreateBlogRequestBody(url = "")

        val response = webTestClient.post()
            .uri("/blogs")
            .body(Mono.just(body), CreateBlogRequestBody::class.java)
            .header("Content-Type", "application/json")
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody!!

        blogRepository.findAll().size shouldBeEqualTo 0
        assertResponse(response).matchesJsonFile("create_blog_empty_url_response.json")
    }

    @Test
    fun `When creating a new blog with an invalid url returns a response with error`() {
        val body = CreateBlogRequestBody(url = "https://stanete.")

        val response = webTestClient.post()
            .uri("/blogs")
            .body(Mono.just(body), CreateBlogRequestBody::class.java)
            .header("Content-Type", "application/json")
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody!!

        blogRepository.findAll().size shouldBeEqualTo 0
        assertResponse(response).matchesJsonFile("create_blog_invalid_url_response.json")
    }
}
