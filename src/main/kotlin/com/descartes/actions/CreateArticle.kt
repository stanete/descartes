package com.descartes.actions

import com.descartes.articles.Article
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class CreateArticle {

    operator fun invoke(url: String): Article {
        Logger.getGlobal().info("Creating article with url $url.")
        return Article(url)
    }
}
