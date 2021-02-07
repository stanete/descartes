package com.descartes

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.skrape
import org.springframework.stereotype.Service

@Service
class ScrapeArticle {

    suspend operator fun invoke(url: String): String = skrape(HttpFetcher) {
        request { this.url = url }
        extract {
            htmlDocument { text }
        }
    }
}
