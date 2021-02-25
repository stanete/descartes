package com.descartes.articles

import com.descartes.concepts.Concept
import com.descartes.topics.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, String> {

    @Query("SELECT DISTINCT(a), COUNT(t) FROM Article a INNER JOIN a.topics t " +
        "WHERE t IN :topics GROUP BY a.url ORDER BY COUNT(t) DESC")
    fun findAllByTopics(
        @Param("topics") topics: Set<Topic>,
    ): List<Article>


    @Query("SELECT DISTINCT(a), COUNT(c) FROM Article a INNER JOIN a.concepts c " +
        "WHERE c IN :concepts GROUP BY a.url ORDER BY COUNT(c) DESC")
    fun findAllByConcepts(
        @Param("concepts") topics: Set<Concept>,
    ): List<Article>
}

