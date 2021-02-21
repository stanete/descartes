package com.descartes.analyser

import com.textrazor.TextRazor
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("test")
class AnalyserTestConfiguration(@Value("\${analyser.apiKey}") val apiKey: String) {

    @Bean
    fun analyser() = mockk<TextRazor>()
}
