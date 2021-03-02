package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import com.descartes.blogs.Blog
import com.descartes.blogs.BlogRepository
import com.descartes.topics.Topic
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@ContextConfiguration
class UpdateArticleTest {

    @Autowired
    private lateinit var blogRepository: BlogRepository

    @Autowired
    private lateinit var repository: ArticleRepository
    private val url = "https://stanete.com/system-design-101"

    @Test
    fun `When invoking updates article in repository with content and returns it`() {
        val blog = blogRepository.save(Blog(url = "https://stanete.com/"))
        repository.save(Article(url, blog))
        val updateArticle = UpdateArticle(repository)

        val articleToUpdate = Article(url, blog, content = "System Design 101 [...] on GitHub © 2021 David Stanete")
        val updatedArticle = updateArticle(articleToUpdate)

        val expectedContent = "System Design 101 [...] on GitHub © 2021 David Stanete"
        updatedArticle.content shouldBeEqualTo expectedContent
        val article = repository.getOne(url)
        article.content shouldBeEqualTo expectedContent
    }

    @Test
    fun `When invoking updates article in repository with topics and returns it`() {
        val blog = blogRepository.save(Blog(url = "https://stanete.com/"))
        val savedArticle = repository.save(
            Article(url, blog, content = "System Design 101 [...] on GitHub © 2021 David Stanete")
        )
        val updateArticle = UpdateArticle(repository)

        val articleToUpdate = Article(savedArticle.url, savedArticle.blog, savedArticle.content)
        articleToUpdate.addTopic(Topic("System Design"))
        articleToUpdate.addTopic(Topic("Engineering"))
        val updatedArticle = updateArticle(articleToUpdate)

        val expectedContent = "System Design 101 [...] on GitHub © 2021 David Stanete"
        val expectedTopics = mutableSetOf(Topic("System Design"), Topic("Engineering"))
        updatedArticle.content shouldBeEqualTo expectedContent
        updatedArticle.topics shouldBeEqualTo expectedTopics
        val article = repository.getOne(url)
        article.content shouldBeEqualTo expectedContent
        article.topics shouldBeEqualTo expectedTopics
    }
}
