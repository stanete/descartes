package com.descartes.mqtp

import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate

class PublisherTest {

    private val rabbitTemplate = mockk<RabbitTemplate>()
    private val publisher = Publisher(rabbitTemplate)

    @Test
    fun `When sending message to queue calls rabbit template`() {
        val message = Message(mapOf("url" to "https://stanete.com/system-design-101"))
        justRun { rabbitTemplate.convertAndSend(Rabbitmq.SCRAPE_ARTICLE, message.toJson()) }

        publisher.send(Rabbitmq.SCRAPE_ARTICLE, message = message.toJson())

        verify { rabbitTemplate.convertAndSend(Rabbitmq.SCRAPE_ARTICLE, message.toJson()) }
    }
}
