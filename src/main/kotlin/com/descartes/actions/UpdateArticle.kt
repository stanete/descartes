package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class UpdateArticle(private val articleRepository: ArticleRepository) {

    operator fun invoke(article: Article): Article {
        Logger.getGlobal().info("Updating article $article.")
        return articleRepository.save(article)
    }
}
