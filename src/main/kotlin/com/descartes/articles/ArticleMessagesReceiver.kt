package com.descartes.articles

import com.descartes.mqtp.Message
import com.descartes.mqtp.Rabbitmq
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ArticleMessagesReceiver(val presenter: ArticlePresenter) {

    @RabbitListener(queues = [Rabbitmq.SCRAPE_ARTICLE])
    fun scrape(message: String) {
        val data = jacksonObjectMapper().readValue(message, Message::class.java).data
        data["url"]?.let { presenter.scrape(it) }
    }
}
