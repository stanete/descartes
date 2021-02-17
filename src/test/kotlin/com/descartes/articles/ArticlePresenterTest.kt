package com.descartes.articles

import com.descartes.actions.CreateArticle
import com.descartes.actions.PublishMessage
import com.descartes.actions.ScrapeArticle
import com.descartes.actions.UpdateArticle
import com.descartes.mqtp.Message
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class ArticlePresenterTest {

    private val publishMessage = mockk<PublishMessage>(relaxed = true)
    private val createArticle = mockk<CreateArticle>()
    private val scrapeArticle = mockk<ScrapeArticle>()
    private val updateArticle = mockk<UpdateArticle>()

    private val presenter = ArticlePresenter(publishMessage, createArticle, scrapeArticle, updateArticle)

    @Test
    fun `When creating article publishes message with url to the correct queue`() = runBlocking<Unit> {
        val url = "https://stanete.com/system-design-101"
        every { createArticle(url) } returns Article(url)

        val article = presenter.create(url)

        verify { createArticle(url) }
        verify { publishMessage(queueName = "scrapeArticle", message = Message(mapOf("url" to url))) }
        article shouldBeEqualTo Article(url)
    }

    @Test
    fun `When scraping article updates it with text`() {
        val url = "https://stanete.com/system-design-101"
        val content = "System Design 101 [...] on GitHub © 2021 David Stanete"
        every { scrapeArticle(url) } returns content
        every { updateArticle(Article(url, content)) }
    }
}
