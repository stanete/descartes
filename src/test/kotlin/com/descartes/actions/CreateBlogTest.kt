package com.descartes.actions

import com.descartes.blogs.BlogRepository
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@ContextConfiguration
@ExtendWith(SpringExtension::class)
class CreateBlogTest {

    @Autowired
    private lateinit var repository: BlogRepository

    @Test
    fun `When invoking creates blog in repository with a clean url and returns it`() {
        val createBlog = CreateBlog(repository)
        val url = "https://stanete.com/system-design-101?utm_source=active%20users&utm_medium=email"

        val createdBlog = createBlog(url)

        val expectedUrl = "https://stanete.com/"
        createdBlog.url shouldBeEqualTo expectedUrl
        val blog = repository.getOne(expectedUrl)
        blog.url shouldBeEqualTo expectedUrl
    }
}
