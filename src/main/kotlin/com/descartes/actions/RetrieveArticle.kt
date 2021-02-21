package com.descartes.actions

import com.descartes.articles.Article
import com.descartes.articles.ArticleRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class RetrieveArticle(private val repository: ArticleRepository) {

    operator fun invoke(url: String): Result<Article, Throwable> = try {
        Ok(repository.findById(url).get())
    } catch (e: NoSuchElementException) {
        Err(ArticleNotFound(url))
    }
}

class ArticleNotFound(url: String) : Throwable(
    "Unable to find Article with url: $url."
)
