package com.descartes.actions

import com.descartes.blogs.Blog
import com.descartes.blogs.BlogRepository
import com.descartes.extensions.baseUrl
import org.springframework.stereotype.Service

@Service
class RetrieveBlog(val repository: BlogRepository) {

    operator fun invoke(url: String): Blog = repository.findById(url.baseUrl()).get()
}
