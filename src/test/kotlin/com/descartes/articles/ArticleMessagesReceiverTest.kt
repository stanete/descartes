package com.descartes.articles

import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ArticleMessagesReceiverTest {

    private val presenter = mockk<ArticlePresenter>()
    private val receiver = ArticleMessagesReceiver(presenter)

    @Test
    fun `When receiving message with url calls the presenter to scrape an article`() {
        val message = "{\"data\":{\"url\":\"https://stanete.com/system-design-101\"}}"
        justRun { presenter.scrape("https://stanete.com/system-design-101") }

        receiver.scrape(message)

        verify { presenter.scrape("https://stanete.com/system-design-101") }
    }

    @Test
    fun `When receiving message without url does not call the presenter to scrape an article`() {
        val message = "{\"data\":{}}"

        receiver.scrape(message)

        verify(exactly = 0) { presenter.scrape(any()) }
    }
}
