package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@ContextConfiguration
@ExtendWith(SpringExtension::class)
class UpdateArticleTest {

    @Autowired
    private lateinit var repository: ArticleRepository
    private val url = "https://stanete.com/system-design-101"

    @BeforeEach
    private fun setUp() {
        repository.save(Article(url))
    }

    @Test
    fun `When invoking updates article in repository with content and returns it`() {
        val updateArticle = UpdateArticle(repository)

        val articleToUpdate = Article(url, content = "System Design 101 [...] on GitHub © 2021 David Stanete")
        val updatedArticle = updateArticle(articleToUpdate)

        val expectedContent = "System Design 101 [...] on GitHub © 2021 David Stanete"
        updatedArticle.content shouldBeEqualTo expectedContent
        val article = repository.getOne(url)
        article.content shouldBeEqualTo expectedContent
    }
}
