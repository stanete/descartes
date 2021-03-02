package com.descartes.articles

import com.descartes.actions.AnalyseArticle
import com.descartes.actions.CreateArticle
import com.descartes.actions.CreateRecommendations
import com.descartes.actions.PublishMessage
import com.descartes.actions.RetrieveArticle
import com.descartes.actions.RetrieveBlog
import com.descartes.actions.ScrapeArticle
import com.descartes.actions.UpdateArticle
import com.descartes.mqtp.Message
import com.descartes.mqtp.Rabbitmq
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.get
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class ArticlePresenter(
    private val retrieveBlog: RetrieveBlog,
    private val createArticle: CreateArticle,
    private val publishMessage: PublishMessage,
    private val scrapeArticle: ScrapeArticle,
    private val updateArticle: UpdateArticle,
    private val retrieveArticle: RetrieveArticle,
    private val analyseArticle: AnalyseArticle,
    private val createRecommendations: CreateRecommendations,
) {

    suspend fun create(url: String): Article {
        val blog = retrieveBlog(url)
        val article = createArticle(url, blog)
        val message = Message(mapOf("url" to article.url))
        publishMessage(queueName = Rabbitmq.SCRAPE_ARTICLE, message)
        return article
    }

    fun scrape(url: String) {
        val blog = retrieveBlog(url)
        val content = scrapeArticle(url)
        val article = Article(url, blog, content)
        updateArticle(article)
        val message = Message(mapOf("url" to article.url))
        publishMessage(queueName = Rabbitmq.ANALYSE_ARTICLE, message)
    }

    fun analyse(url: String) {
        retrieveArticle(url)
            .onSuccess {
                analyseArticle(it)
                    .onSuccess {
                        updateArticle(it)
                        val message = Message(mapOf("url" to it.url))
                        publishMessage(queueName = Rabbitmq.CREATE_RECOMMENDATIONS, message)
                    }
                    .onFailure { Logger.getGlobal().warning(it.message) }
            }
            .onFailure { Logger.getGlobal().warning(it.message) }
    }

    fun createRecommendations(url: String) {
        retrieveArticle(url)
            .onSuccess {
                createRecommendations(it)
                    .onFailure { Logger.getGlobal().warning(it.message) }
            }
            .onFailure { Logger.getGlobal().warning(it.message) }
    }
}
