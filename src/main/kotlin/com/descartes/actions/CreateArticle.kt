package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import com.descartes.blogs.Blog
import com.descartes.extensions.withoutUrlParameters
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class CreateArticle(private val repository: ArticleRepository) {

    operator fun invoke(url: String, blog: Blog): Article {
        val urlWithoutParameters = url.withoutUrlParameters()
        Logger.getGlobal().info("Creating article with url $urlWithoutParameters.")
        return repository.save(Article(urlWithoutParameters, blog))
    }
}
