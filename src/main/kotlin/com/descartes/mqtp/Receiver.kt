package com.descartes.mqtp

import com.descartes.mqtp.Rabbitmq.Companion.SCRAPE_ARTICLE
import org.springframework.stereotype.Component
import org.springframework.amqp.rabbit.annotation.RabbitListener

@Component
class Receiver {

    @RabbitListener(queues = [SCRAPE_ARTICLE])
    fun listen(message: String) {
        println("Message read from $SCRAPE_ARTICLE: $message")
    }
}
