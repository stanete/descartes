package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.topics.Topic
import com.textrazor.TextRazor
import org.springframework.stereotype.Service

@Service
class AnalyseArticle(val analyser: TextRazor) {

    operator fun invoke(article: Article): Article {
        val analyzedContent = analyser.apply {
            extractors = listOf(EXTRACTOR_ENTITIES, EXTRACTOR_TOPICS)
        }.analyze(article.content)

        val language = analyzedContent.response.language
        val topics = analyzedContent.response.topics.filter { it.score > MINIMUM_SCORE }

        return Article(article.url, article.content, language = language).apply {
            topics.forEach { addTopic(Topic(it.label, it.wikiLink)) }
        }
    }

    companion object {
        const val EXTRACTOR_ENTITIES = "entities"
        const val EXTRACTOR_TOPICS = "topics"
        const val MINIMUM_SCORE = 0.9F
    }
}
