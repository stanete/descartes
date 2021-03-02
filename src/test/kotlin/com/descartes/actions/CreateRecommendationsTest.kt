package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import com.descartes.blogs.Blog
import com.descartes.concepts.Concept
import com.descartes.topics.Topic
import com.github.michaelbull.result.getError
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class CreateRecommendationsTest {

    private val repository = mockk<ArticleRepository>()
    private val createRecommendations = CreateRecommendations(repository)

    @Test
    fun `When articles with concepts and topics in common exist creates recommendations for given article`() {
        val article = articleWithTopicsAndConcepts()
        val articlesFromTopics = articlesFoundByTopics()
        val articlesFromConcepts = articlesFoundByConcepts()
        val articleWithRecommendations = articleWithRecommendations(articlesFromTopics, articlesFromConcepts)
        every { repository.findAllByTopics(article.topics) } returns articlesFromTopics
        every { repository.findAllByConcepts(article.concepts) } returns articlesFromConcepts
        every { repository.save(articleWithRecommendations) } returns articleWithRecommendations

        createRecommendations(article)

        verify { repository.findAllByConcepts(article.concepts) }
        verify { repository.findAllByTopics(article.topics) }
        verify { repository.save(articleWithRecommendations) }
    }

    @Test
    fun `When articles with concepts and topics in common do not exist returns Err wrapping throwable`() {
        val article = articleWithTopicsAndConcepts()
        every { repository.findAllByTopics(article.topics) } returns emptyList()
        every { repository.findAllByConcepts(article.concepts) } returns emptyList()

        val result = createRecommendations(article)

        verify { repository.findAllByConcepts(article.concepts) }
        verify { repository.findAllByTopics(article.topics) }
        verify(exactly = 0) { repository.save(any()) }
        result.getError() shouldBeInstanceOf RecommendationsNotCreated::class
    }

    private fun articleWithTopicsAndConcepts() = Article(
        url = "https://stanete.com/system-design-101",
        blog = Blog(url = "https://stanete.com/")
    ).apply {
        addTopic(Topic(label = "System Design"))
        addTopic(Topic(label = "Networking"))
        addTopic(Topic(label = "Programming"))
        addTopic(Topic(label = "Computer Science"))
        addConcept(Concept(label = "CDN"))
        addConcept(Concept(label = "Amazon Web Services"))
        addConcept(Concept(label = "PostgreSQL"))
        addConcept(Concept(label = "Docker"))
    }

    private fun articlesFoundByTopics(): List<Article> = listOf(
        Article(
            url = "https://stanete.com/system-design-102",
            blog = Blog(url = "https://stanete.com/")
        ).apply {
            // Article with a lot of topics that other articles don't have.
            addTopic(Topic(label = "Programming"))
            addTopic(Topic(label = "Engineering"))
            addTopic(Topic(label = "Architecture"))
            addTopic(Topic(label = "Computer Systems"))
            addTopic(Topic(label = "Software"))
        },
        Article(
            url = "https://stanete.com/system-design-103",
            blog = Blog(url = "https://stanete.com/")
        ).apply {
            addTopic(Topic(label = "System Design"))
            addTopic(Topic(label = "Networking"))
            addTopic(Topic(label = "Engineering"))
            addTopic(Topic(label = "Computer Science"))
        },
        Article(
            url = "https://stanete.com/system-design-104",
            blog = Blog(url = "https://stanete.com/")
        ).apply {
            addTopic(Topic(label = "System Design"))
            addTopic(Topic(label = "Networking"))
            addTopic(Topic(label = "Engineering"))
        }
    )

    private fun articlesFoundByConcepts(): List<Article> = listOf(
        Article(
            url = "https://stanete.com/system-design-102",
            blog = Blog(url = "https://stanete.com/")
        ).apply {
            // Article with a lot of concepts that other articles don't have.
            addConcept(Concept(label = "PostgreSQL"))
            addConcept(Concept(label = "World Wide Web"))
            addConcept(Concept(label = "Netlify"))
            addConcept(Concept(label = "DevOps"))
            addConcept(Concept(label = "IP address"))
        },
        Article(
            url = "https://stanete.com/system-design-103",
            blog = Blog(url = "https://stanete.com/")
        ).apply {
            addConcept(Concept(label = "CDN"))
            addConcept(Concept(label = "Amazon Web Services"))
            addConcept(Concept(label = "World Wide Web"))
            addConcept(Concept(label = "Docker"))
        },
        Article(
            url = "https://stanete.com/system-design-104",
            blog = Blog(url = "https://stanete.com/")
        ).apply {
            addConcept(Concept(label = "CDN"))
            addConcept(Concept(label = "Amazon Web Services"))
            addConcept(Concept(label = "World Wide Web"))
        }
    )

    private fun articleWithRecommendations(
        articlesFromTopics: List<Article>,
        articlesFromConcepts: List<Article>
    ): Article {
        val article = articleWithTopicsAndConcepts()
        val articles = articlesFromTopics.intersect(articlesFromConcepts).apply {
            plus(articlesFromConcepts.minus(this))
            plus(articlesFromTopics.minus(this))
        }

        articles.forEach { article.addRecommendation(it) }
        return article
    }
}
