package com.descartes.analyser

import com.textrazor.TextRazor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class AnalyserConfiguration(@Value("\${analyser.apiKey}") val apiKey: String) {

    @Bean
    fun analyser() = TextRazor(apiKey)
}
