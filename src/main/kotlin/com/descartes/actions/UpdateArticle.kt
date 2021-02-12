package com.descartes.actions

import com.descartes.articles.Article
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class UpdateArticle {

    operator fun invoke(article: Article): Article {
        Logger.getGlobal().info("Updating article $article")
        return article
    }
}
