package com.descartes.concepts

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConceptRepository : JpaRepository<Concept, String>
