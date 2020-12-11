package com.example.venka.eventio.data.db

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.Entity
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * Creates unique constraints for some entities.
 */
@Monitor
interface ConstraintController : Neo4jRepository<Entity<String, String>, String> {

    @Query("CREATE CONSTRAINT ON (n:Person) ASSERT n.email IS UNIQUE")
    fun emailConstraint()

    @Query("CREATE CONSTRAINT ON (e:Event) ASSERT e.title IS UNIQUE")
    fun titleConstraint()

    @Query("CREATE CONSTRAINT ON (p:Place) ASSERT p.realAddress IS UNIQUE")
    fun addressConstraint()
}
