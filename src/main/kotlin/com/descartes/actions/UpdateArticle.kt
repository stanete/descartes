package com.descartes.actions

import com.descartes.articles.Article
import org.springframework.stereotype.Service

@Service
class UpdateArticle {

    operator fun invoke(article: Article): Article {
        return article
    }
}
