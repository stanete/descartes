package com.descartes.actions

import com.descartes.articles.ArticleRepository
import com.descartes.blogs.Blog
import com.descartes.blogs.BlogRepository
import org.amshove.kluent.shouldBeEqualTo
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
class CreateArticleTest {

    @Autowired
    private lateinit var blogRepository: BlogRepository

    @Autowired
    private lateinit var repository: ArticleRepository

    @Test
    fun `When invoking creates article in repository with a clean url and returns it`() {
        val blog = blogRepository.save(Blog(url = "https://stanete.com"))
        val createArticle = CreateArticle(repository)
        val url = "https://stanete.com/system-design-101?utm_source=active%20users&utm_medium=email"

        val createdArticle = createArticle(url, blog)

        val expectedUrl = "https://stanete.com/system-design-101"
        createdArticle.url shouldBeEqualTo expectedUrl
        val article = repository.getOne(expectedUrl)
        article.url shouldBeEqualTo expectedUrl
    }
}
