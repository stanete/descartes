package com.descartes.articles

import com.descartes.topics.Topic
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Test

class ArticleTest {

    val url = "https://stanete.com/system-design-101"
    private val article: Article = Article(url)

    @Test
    fun `When adding topic ads itself to topic's articles`() {
        val topic = Topic(label = "System Design")

        article.addTopic(topic)

        article.topics shouldContain topic
        topic.articles shouldContain article
    }
}
