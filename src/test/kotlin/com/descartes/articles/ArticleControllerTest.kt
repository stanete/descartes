package com.descartes.articles

import com.descartes.ApiContractAssertion.Companion.assertResponse
import com.descartes.ArticleHttpStub
import com.descartes.getStubContent
import com.descartes.stub
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

    @Test
    fun `When creating a new article with a url return a response with the created article`() {
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
