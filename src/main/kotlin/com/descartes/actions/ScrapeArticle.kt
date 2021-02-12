package com.descartes.actions

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.skrape
import org.springframework.stereotype.Service

@Service
class ScrapeArticle {

    operator fun invoke(url: String): String = skrape(HttpFetcher) {
        request { this.url = url }
        extract {
            htmlDocument { text }
        }
    }
}
