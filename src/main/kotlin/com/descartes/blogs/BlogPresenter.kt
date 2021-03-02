package com.descartes.blogs

import com.descartes.actions.CreateBlog
import org.springframework.stereotype.Service

@Service
class BlogPresenter(val createBlog: CreateBlog) {
    suspend fun create(url: String): Blog {
        return createBlog(url)
    }
}
