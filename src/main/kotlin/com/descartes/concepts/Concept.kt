package com.descartes.concepts

import com.descartes.articles.Article
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "concepts")
data class Concept(
    @Id val label: String,
    val wikiLink: String? = null,
) {
    @ManyToMany(mappedBy = "concepts")
    val articles: MutableSet<Article> = HashSet()
}
