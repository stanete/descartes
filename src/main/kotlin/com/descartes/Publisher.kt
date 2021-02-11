package com.descartes

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class Publisher(val rabbitTemplate: RabbitTemplate) {

    fun send(queueName: String, message: Message) {
        rabbitTemplate.convertAndSend(queueName, jacksonObjectMapper().writeValueAsString(message))
    }
}
