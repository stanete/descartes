package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@ContextConfiguration
class RetrieveArticleTest {

    @Autowired
    private lateinit var repository: ArticleRepository
    private val url = "https://stanete.com/system-design-101"

    @Test
    fun `When invoking returns Ok wrapping article by url`() {
        val retrieveArticle = RetrieveArticle(repository)
        val savedArticle = repository.save(
            Article(url, content = "System Design 101 [...] on GitHub © 2021 David Stanete")
        )

        val result = retrieveArticle(url)

        val retrievedArticle = result.get()!!
        retrievedArticle.url shouldBeEqualTo url
        retrievedArticle.url shouldBeEqualTo savedArticle.url
    }

    @Test
    fun `When invoking and article does not exist returns Err wrapping throwable`() {
        val retrieveArticle = RetrieveArticle(repository)

        val result = retrieveArticle(url)

        result.getError() shouldBeInstanceOf ArticleNotFound::class
    }
}
