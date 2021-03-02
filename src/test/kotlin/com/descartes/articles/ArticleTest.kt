package com.descartes.articles

import com.descartes.blogs.Blog
import com.descartes.concepts.Concept
import com.descartes.topics.Topic
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Test

class ArticleTest {

    val url = "https://stanete.com/system-design-101"
    private val blog = Blog(url = "https://stanete.com/")
    private val article: Article = Article(url, blog)

    @Test
    fun `When adding topic ads itself to topic's articles`() {
        val topic = Topic(label = "System Design")

        article.addTopic(topic)

        article.topics shouldContain topic
        topic.articles shouldContain article
    }

    @Test
    fun `When adding concept ads itself to concept's articles`() {
        val concept = Concept(label = "CDN")

        article.addConcept(concept)

        article.concepts shouldContain concept
        concept.articles shouldContain article
    }

    @Test
    fun `When adding recommendation ads itself to recommendation's articles`() {
        val recommendedArticle = Article(url = "https://stanete.com/system-design-102", blog = blog)

        article.addRecommendation(recommendedArticle)

        article.recommendations shouldContain recommendedArticle
        recommendedArticle.recommendations shouldContain article
    }
}
