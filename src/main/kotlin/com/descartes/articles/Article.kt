package com.descartes.articles

import com.descartes.concepts.Concept
import com.descartes.topics.Topic
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.CascadeType
import javax.persistence.JoinTable
import javax.persistence.JoinColumn

@Entity
@Table(name = "articles")
data class Article(
    @Id val url: String,
    val content: String? = null,
    val language: String? = null,
) {
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "articles_topics",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "topic_id")],
    )
    val topics: MutableSet<Topic> = HashSet()

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "articles_concepts",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "concept_id")],
    )
    val concepts: MutableSet<Concept> = HashSet()

    fun addTopic(topic: Topic) {
        topics.add(topic)
        topic.articles.add(this)
    }

    fun addConcept(concept: Concept) {
        concepts.add(concept)
        concept.articles.add(this)
    }
}
