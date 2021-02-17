package com.descartes.articles

import com.descartes.actions.CreateArticle
import com.descartes.actions.PublishMessage
import com.descartes.actions.ScrapeArticle
import com.descartes.actions.UpdateArticle
import com.descartes.mqtp.Message
import com.descartes.mqtp.Rabbitmq
import org.springframework.stereotype.Service

@Service
class ArticlePresenter(
    private val publishMessage: PublishMessage,
    private val createArticle: CreateArticle,
    private val scrapeArticle: ScrapeArticle,
    private val updateArticle: UpdateArticle,
) {

    suspend fun create(url: String): Article {
        val article = createArticle(url)
        val message = Message(mapOf("url" to article.url))
        publishMessage(queueName = Rabbitmq.SCRAPE_ARTICLE, message)
        return article
    }

    fun scrape(url: String) {
        val content = scrapeArticle(url)
        val article = Article(url, content)
        updateArticle(article)
    }
}
