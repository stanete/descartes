package com.descartes.mqtp

import io.mockk.mockk
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("test")
class Rabbitmq(@Value("\${cloudamqp.url}") val uri: String) {

    @Bean
    fun publisher(rabbitTemplate: RabbitTemplate) = mockk<Publisher>(relaxed = true)
}
