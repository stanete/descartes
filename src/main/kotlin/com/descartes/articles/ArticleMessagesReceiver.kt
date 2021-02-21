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
        println("Message read from ${Rabbitmq.SCRAPE_ARTICLE}: $message.")
        val data = jacksonObjectMapper().readValue(message, Message::class.java).data
        data["url"]?.let { presenter.scrape(it) }
    }

    @RabbitListener(queues = [Rabbitmq.ANALYSE_ARTICLE])
    fun analyse(message: String) {
        println("Message read from ${Rabbitmq.ANALYSE_ARTICLE}: $message.")
        val data = jacksonObjectMapper().readValue(message, Message::class.java).data
        data["url"]?.let { presenter.analyse(it) }
    }
}
