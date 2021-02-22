package com.descartes.articles

import com.descartes.actions.CreateArticle
import com.descartes.actions.PublishMessage
import com.descartes.actions.ScrapeArticle
import com.descartes.actions.UpdateArticle
import com.descartes.actions.RetrieveArticle
import com.descartes.actions.AnalyseArticle
import com.descartes.mqtp.Message
import com.descartes.mqtp.Rabbitmq
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class ArticlePresenter(
    private val createArticle: CreateArticle,
    private val publishMessage: PublishMessage,
    private val scrapeArticle: ScrapeArticle,
    private val updateArticle: UpdateArticle,
    private val retrieveArticle: RetrieveArticle,
    private val analyseArticle: AnalyseArticle,
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
        val message = Message(mapOf("url" to article.url))
        publishMessage(queueName = Rabbitmq.ANALYSE_ARTICLE, message)
    }

    fun analyse(url: String) {
        retrieveArticle(url)
            .onSuccess {
                analyseArticle(it)
                    .onSuccess { updateArticle(it) }
                    .onFailure { Logger.getGlobal().warning(it.message) }
            }
            .onFailure { Logger.getGlobal().warning(it.message) }
    }
}
