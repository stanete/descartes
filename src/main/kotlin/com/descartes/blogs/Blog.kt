package com.descartes.blogs

import com.descartes.articles.Article
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "blogs")
data class Blog(
    @Id val url: String,
) {
    @OneToMany(mappedBy = "blog", cascade = [CascadeType.ALL], orphanRemoval = true)
    val articles: MutableSet<Article> = HashSet()
}
