package com.descartes.articles

import com.descartes.ApiContractAssertion.Companion.assertResponse
import com.descartes.ArticleHttpStub
import com.descartes.blogs.Blog
import com.descartes.blogs.BlogRepository
import com.descartes.getStubContent
import com.descartes.stub
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@SpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension::class)
@AutoConfigureWireMock(port = 8089)
class ArticleControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var blogRepository: BlogRepository

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @BeforeEach
    fun tearDown() {
        blogRepository.save(Blog(url = "https://stanete.com/"))
    }

    @AfterEach
    fun setUp() {
        /**
         * Automatic rollback with @Transaction annotation is not supported when applied directly from
         * the "web layer". Using either RANDOM_PORT or DEFINED_PORT implicitly provides a real servlet environment.
         * HTTP client and server will run in separate threads, thus separate transactions. Any transaction initiated
         * on the server won’t rollback in this case. So the database needs to be cleaned manually.
         */
        articleRepository.deleteAll()
        blogRepository.deleteAll()
    }

    @Test
    fun `When creating a new article with a url return a response with the created article`() {
        blogRepository.save(Blog(url = "https://stanete.com/"))
        val url = "https://stanete.com/system-design-101"
        stub(
            ArticleHttpStub(
                url = url,
                responseBody = getStubContent("scrap_article_response.html")
            )
        )

        val body = CreateArticleRequestBody(url = url)
        val response = webTestClient.post()
            .uri("/articles")
            .body(Mono.just(body), CreateArticleRequestBody::class.java)
            .header("Content-Type", "application/json")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody!!

        assertResponse(response).matchesJsonFile("create_article_response.json")
    }
}
