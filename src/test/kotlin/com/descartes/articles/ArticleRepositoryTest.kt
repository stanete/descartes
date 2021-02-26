package com.descartes.articles

import com.descartes.concepts.Concept
import com.descartes.concepts.ConceptRepository
import com.descartes.topics.Topic
import com.descartes.topics.TopicRepository
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
class ArticleRepositoryTest {

    @Autowired
    lateinit var repository: ArticleRepository

    @Autowired
    lateinit var topicRepository: TopicRepository

    @Autowired
    lateinit var conceptRepository: ConceptRepository

    @Test
    fun `When finding articles by topics returns list of articles ordered by count of topics in given set`() {
        setUpArticlesWithTopics()
        val topics = topicRepository.findAllById(
            setOf("System Design", "Networking", "Programming", "Computer Science")
        ).toSet()

        val articles = repository.findAllByTopics(topics)

        articles.size shouldBeEqualTo 4
        articles[0].url shouldBeEqualTo "https://stanete.com/system-design-101"
        articles[1].url shouldBeEqualTo "https://stanete.com/system-design-103"
        articles[2].url shouldBeEqualTo "https://stanete.com/system-design-104"
        articles[3].url shouldBeEqualTo "https://stanete.com/system-design-102"
    }

    @Test
    fun `When finding articles by concepts returns list of articles ordered by count of concepts in given set`() {
        setUpArticlesWithConcepts()
        val concepts = conceptRepository.findAllById(
            setOf("CDN", "Amazon Web Services", "PostgreSQL", "Docker")
        ).toSet()

        val articles = repository.findAllByConcepts(concepts)

        articles.size shouldBeEqualTo 4
        articles[0].url shouldBeEqualTo "https://stanete.com/system-design-101"
        articles[1].url shouldBeEqualTo "https://stanete.com/system-design-103"
        articles[2].url shouldBeEqualTo "https://stanete.com/system-design-104"
        articles[3].url shouldBeEqualTo "https://stanete.com/system-design-102"
    }

    @Test
    fun `When finding articles returns all its recommendations`() {
        val savedArticle = repository.save(
            Article(url = "https://stanete.com/system-design-101").apply {
                addTopic(Topic(label = "System Design"))
                addTopic(Topic(label = "Networking"))
                addTopic(Topic(label = "Programming"))
                addTopic(Topic(label = "Computer Science"))
            }
        )
        setUpRecommendationsForArticle(savedArticle)

        val article = repository.findById(savedArticle.url).get()

        article.recommendations.size shouldBeEqualTo 3
    }

    private fun setUpArticlesWithTopics(): List<Article> = repository.saveAll(listOf(
        Article(url = "https://stanete.com/system-design-101").apply {
            addTopic(Topic(label = "System Design"))
            addTopic(Topic(label = "Networking"))
            addTopic(Topic(label = "Programming"))
            addTopic(Topic(label = "Computer Science"))
        },
        Article(url = "https://stanete.com/system-design-102").apply {
            // Article with a lot of topics that other articles don't have.
            addTopic(Topic(label = "Programming"))
            addTopic(Topic(label = "Engineering"))
            addTopic(Topic(label = "Architecture"))
            addTopic(Topic(label = "Computer Systems"))
            addTopic(Topic(label = "Software"))
        },
        Article(url = "https://stanete.com/system-design-103").apply {
            addTopic(Topic(label = "System Design"))
            addTopic(Topic(label = "Networking"))
            addTopic(Topic(label = "Engineering"))
            addTopic(Topic(label = "Computer Science"))
        },
        Article(url = "https://stanete.com/system-design-104").apply {
            addTopic(Topic(label = "System Design"))
            addTopic(Topic(label = "Networking"))
            addTopic(Topic(label = "Engineering"))
        }
    ))

    private fun setUpArticlesWithConcepts(): List<Article> = repository.saveAll(listOf(
        Article(url = "https://stanete.com/system-design-101").apply {
            addConcept(Concept(label = "CDN"))
            addConcept(Concept(label = "Amazon Web Services"))
            addConcept(Concept(label = "PostgreSQL"))
            addConcept(Concept(label = "Docker"))
        },
        Article(url = "https://stanete.com/system-design-102").apply {
            // Article with a lot of concepts that other articles don't have.
            addConcept(Concept(label = "PostgreSQL"))
            addConcept(Concept(label = "World Wide Web"))
            addConcept(Concept(label = "Netlify"))
            addConcept(Concept(label = "DevOps"))
            addConcept(Concept(label = "IP address"))
        },
        Article(url = "https://stanete.com/system-design-103").apply {
            addConcept(Concept(label = "CDN"))
            addConcept(Concept(label = "Amazon Web Services"))
            addConcept(Concept(label = "World Wide Web"))
            addConcept(Concept(label = "Docker"))
        },
        Article(url = "https://stanete.com/system-design-104").apply {
            addConcept(Concept(label = "CDN"))
            addConcept(Concept(label = "Amazon Web Services"))
            addConcept(Concept(label = "World Wide Web"))
        }
    ))

    private fun setUpRecommendationsForArticle(article: Article): List<Article> {
        val articles = repository.saveAll(listOf(
            Article(url = "https://stanete.com/system-design-102").apply {
                // Article with a lot of topics that other articles don't have.
                addTopic(Topic(label = "Programming"))
                addTopic(Topic(label = "Engineering"))
                addTopic(Topic(label = "Architecture"))
                addTopic(Topic(label = "Computer Systems"))
                addTopic(Topic(label = "Software"))
            },
            Article(url = "https://stanete.com/system-design-103").apply {
                addTopic(Topic(label = "System Design"))
                addTopic(Topic(label = "Networking"))
                addTopic(Topic(label = "Engineering"))
                addTopic(Topic(label = "Computer Science"))
            },
            Article(url = "https://stanete.com/system-design-104").apply {
                addTopic(Topic(label = "System Design"))
                addTopic(Topic(label = "Networking"))
                addTopic(Topic(label = "Engineering"))
            }
        ))

        articles.forEach { article.addRecommendation(it) }

        return repository.saveAll(articles)
    }
}
