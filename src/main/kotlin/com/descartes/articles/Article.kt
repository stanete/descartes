package com.descartes.articles

import com.descartes.topics.Topic
import java.util.logging.Logger
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

    fun addTopic(topic: Topic) {
        Logger.getGlobal().info("Adding topic $topic.")
        topics.add(topic)
        topic.articles.add(this)
    }
}
