package com.descartes.topics

import com.descartes.articles.Article
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
@Table(name = "topics")
data class Topic(
    @Id val label: String,
    val wikiLink: String? = null,
) {
    @ManyToMany(mappedBy = "topics")
    val articles: MutableSet<Article> = HashSet()
}
