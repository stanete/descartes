package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service

@Service
class CreateRecommendations(
    val articleRepository: ArticleRepository
) {

    operator fun invoke(article: Article): Result<Article, Throwable> {
        val articlesFromTopics = articleRepository.findAllByTopics(article.topics).minus(article)
        val articlesFromConcepts = articleRepository.findAllByConcepts(article.concepts).minus(article)

        if (articlesFromConcepts.isEmpty() or articlesFromConcepts.isEmpty()) {
            return Err(RecommendationsNotCreated(url = article.url))
        }

        val articles = articlesFromTopics.intersect(articlesFromConcepts).apply {
            plus(articlesFromConcepts.minus(this))
            plus(articlesFromTopics.minus(this))
        }

        articles.forEach { article.addRecommendation(it) }
        return Ok(articleRepository.save(article))
    }
}

class RecommendationsNotCreated(url: String) : Throwable(
    "Unable to create recommendations for article with url: $url."
)
