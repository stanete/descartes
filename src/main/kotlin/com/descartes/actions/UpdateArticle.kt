package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import org.springframework.stereotype.Service

@Service
class UpdateArticle(private val articleRepository: ArticleRepository) {

    operator fun invoke(article: Article): Article = articleRepository.save(article)
}
