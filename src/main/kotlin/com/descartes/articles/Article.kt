package com.descartes.articles

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "articles")
data class Article(
    @Id val url: String,
    val content: String = ""
)
