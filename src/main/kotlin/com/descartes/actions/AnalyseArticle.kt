package com.descartes.actions

import com.descartes.actions.AnalyseArticle.Companion.MINIMUM_ENTITY_CONFIDENCE_SCORE
import com.descartes.actions.AnalyseArticle.Companion.MINIMUM_ENTITY_RELEVANCE_SCORE
import com.descartes.articles.Article
import com.descartes.concepts.Concept
import com.descartes.topics.Topic
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.textrazor.AnalysisException
import com.textrazor.TextRazor
import com.textrazor.annotations.Entity
import org.springframework.stereotype.Service

@Service
class AnalyseArticle(val analyser: TextRazor) {

    operator fun invoke(article: Article): Result<Article, Throwable> = try {
        val analyzedContent = analyser.apply {
            extractors = listOf(EXTRACTOR_ENTITIES, EXTRACTOR_TOPICS)
        }.analyze(article.content)

        val language = analyzedContent.response.language
        val topics = analyzedContent.response.topics.filter { it.score > MINIMUM_TOPIC_SCORE }
        val entities = analyzedContent.response.entities
            .filter { it.hasEnoughData() && (it.isRelevant() || it.isTrusted()) }
            .distinctBy { it.wikiLink }

        Ok(
            Article(article.url, article.content, language = language).apply {
                topics.forEach { addTopic(Topic(it.label, it.wikiLink)) }
                entities.forEach { addConcept(Concept(label = it.entityEnglishId, wikiLink = it.wikiLink)) }
            }
        )
    } catch (e: AnalysisException) {
        Err(ArticleNotAnalysed(article.url))
    }

    companion object {
        const val EXTRACTOR_ENTITIES = "entities"
        const val EXTRACTOR_TOPICS = "topics"
        const val MINIMUM_TOPIC_SCORE = 0.9F
        const val MINIMUM_ENTITY_RELEVANCE_SCORE = 0.8F
        const val MINIMUM_ENTITY_CONFIDENCE_SCORE = 0.8F
    }
}

fun Entity.isRelevant(): Boolean = relevanceScore > MINIMUM_ENTITY_RELEVANCE_SCORE

fun Entity.isTrusted(): Boolean = confidenceScore > MINIMUM_ENTITY_CONFIDENCE_SCORE

fun Entity.hasEnoughData(): Boolean = entityEnglishId.isNotBlank() && wikiLink.isNotBlank()

class ArticleNotAnalysed(url: String) : Throwable(
    "Unable to analyse Article with url: $url."
)
