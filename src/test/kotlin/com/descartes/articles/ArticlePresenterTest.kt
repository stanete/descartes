package com.descartes.articles

import com.descartes.actions.AnalyseArticle
import com.descartes.actions.ArticleNotAnalysed
import com.descartes.actions.ArticleNotFound
import com.descartes.actions.CreateArticle
import com.descartes.actions.CreateRecommendations
import com.descartes.actions.PublishMessage
import com.descartes.actions.RetrieveArticle
import com.descartes.actions.ScrapeArticle
import com.descartes.actions.UpdateArticle
import com.descartes.concepts.Concept
import com.descartes.mqtp.Message
import com.descartes.mqtp.Rabbitmq
import com.descartes.topics.Topic
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import io.mockk.justRun
import io.mockk.verifyOrder
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class ArticlePresenterTest {

    private val publishMessage = mockk<PublishMessage>()
    private val createArticle = mockk<CreateArticle>()
    private val scrapeArticle = mockk<ScrapeArticle>()
    private val updateArticle = mockk<UpdateArticle>()
    private val getArticle = mockk<RetrieveArticle>()
    private val analyseArticle = mockk<AnalyseArticle>()
    private val createRecommendations = mockk<CreateRecommendations>()

    private val presenter = ArticlePresenter(
        createArticle, publishMessage, scrapeArticle, updateArticle, getArticle, analyseArticle, createRecommendations
    )

    @Test
    fun `When creating article publishes message with url to the correct queue`() = runBlocking<Unit> {
        val url = "https://stanete.com/system-design-101"
        every { createArticle(url) } returns Article(url)
        val message = Message(mapOf("url" to url))
        justRun { publishMessage(Rabbitmq.SCRAPE_ARTICLE, message) }

        val article = presenter.create(url)

        verify { createArticle(url) }
        verify { publishMessage(Rabbitmq.SCRAPE_ARTICLE, message = Message(mapOf("url" to url))) }
        article shouldBeEqualTo Article(url)
    }

    @Test
    fun `When scraping article updates it with text`() {
        val url = "https://stanete.com/system-design-101"
        val content = "System Design 101 [...] on GitHub © 2021 David Stanete"
        every { scrapeArticle(url) } returns content
        val scrapedArticle = Article(url, content)
        every { updateArticle(scrapedArticle) } returns scrapedArticle
        val message = Message(mapOf("url" to url))
        justRun { publishMessage(Rabbitmq.ANALYSE_ARTICLE, message) }

        presenter.scrape(url)

        verifyOrder {
            scrapeArticle(url)
            updateArticle(scrapedArticle)
            publishMessage(Rabbitmq.ANALYSE_ARTICLE, message)
        }
    }

    @Test
    fun `When analysing article updates it with topics and concepts`() {
        val url = "https://stanete.com/system-design-101"
        val content = "System Design 101 [...] on GitHub © 2021 David Stanete"
        val article = Article(url, content)
        every { getArticle(url) } returns Ok(article)
        val topics = mutableSetOf(Topic(label = "System Design"), Topic(label = "Engineering"))
        val concepts = mutableSetOf(Concept(label = "CDN"), Concept(label = "Load Balancer"))
        val analysedArticle = Article(article.url, article.content).apply {
            topics.forEach { addTopic(it) }
            concepts.forEach { addConcept(it) }
        }
        every { analyseArticle(article) } returns Ok(analysedArticle)
        every { updateArticle(analysedArticle) } returns analysedArticle
        val message = Message(mapOf("url" to url))
        justRun { publishMessage(Rabbitmq.CREATE_RECOMMENDATIONS, message) }

        presenter.analyse(url)

        verifyOrder {
            getArticle(url)
            analyseArticle(article)
            updateArticle(analysedArticle)
            publishMessage(Rabbitmq.CREATE_RECOMMENDATIONS, message)
        }
    }

    @Test
    fun `When could not analyse article returns Err wrapping throwable`() {
        val url = "https://stanete.com/system-design-101"
        val content = "System Design 101 [...] on GitHub © 2021 David Stanete"
        val article = Article(url, content)
        every { getArticle(url) } returns Ok(article)
        every { analyseArticle(article) } returns Err(ArticleNotAnalysed(url))

        presenter.analyse(url)

        verifyOrder {
            getArticle(url)
            analyseArticle(article)
        }
        verify(exactly = 0) { updateArticle(any()) }
    }

    @Test
    fun `When analysing article but article could not be found does nothing`() {
        val url = "https://stanete.com/system-design-101"
        every { getArticle(url) } returns Err(ArticleNotFound(url))

        presenter.analyse(url)

        verify { getArticle(url) }
        verify(exactly = 0) { analyseArticle(any()) }
        verify(exactly = 0) { updateArticle(any()) }
        verify(exactly = 0) { publishMessage(any(), any()) }
    }

    @Test
    fun `When creating recommendations for article updates it with recommended articles`() {
        val url = "https://stanete.com/system-design-101"
        val article = Article(url, content = "System Design 101 [...] on GitHub © 2021 David Stanete")
        val articleWithRecommendations = article.copy().apply {
            addRecommendation(Article(url = "https://stanete.com/system-design-102"))
        }
        every { getArticle(url) } returns Ok(article)
        every { createRecommendations(article) } returns Ok(articleWithRecommendations)

        presenter.createRecommendations(url)

        verifyOrder {
            getArticle(url)
            createRecommendations(article)
        }
    }
}
