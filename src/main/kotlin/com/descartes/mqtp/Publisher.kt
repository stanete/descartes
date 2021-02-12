package com.descartes.mqtp

import org.springframework.amqp.rabbit.core.RabbitTemplate

class Publisher(private val rabbitTemplate: RabbitTemplate) {

    fun send(queueName: String, message: String) {
        rabbitTemplate.convertAndSend(queueName, message)
    }
}
