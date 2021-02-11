package com.descartes.configuration

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.beans.factory.annotation.Value

@Configuration
class Rabbitmq(@Value("\${cloudamqp.url}") val uri: String) {

    @Bean
    fun scrapeQueue(): Queue = Queue(SCRAPE_ARTICLE, false)

    @Bean
    fun connectionFactory() = CachingConnectionFactory().apply {
        setUri(uri)
    }

    companion object {
        const val SCRAPE_ARTICLE = "scrapeArticle"
    }
}
