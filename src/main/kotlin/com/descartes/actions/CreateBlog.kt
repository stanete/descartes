package com.descartes.actions

import com.descartes.blogs.Blog
import com.descartes.blogs.BlogRepository
import com.descartes.extensions.baseUrl
import com.descartes.extensions.withoutUrlParameters
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class CreateBlog(private val repository: BlogRepository) {

    operator fun invoke(url: String): Blog {
        val baseUrl = url.withoutUrlParameters().baseUrl()
        Logger.getGlobal().info("Creating blog with url $baseUrl.")
        return repository.save(Blog(baseUrl))
    }
}
