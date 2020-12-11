package com.example.venka.eventio.data.db.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.NaturalPerson
import org.springframework.data.neo4j.annotation.Depth
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * Repository for {@link NaturalPerson} entity
 */
@Monitor
interface NaturalPersonRepository : Neo4jRepository<NaturalPerson, String> {

    /**
     * Returns natural person by email
     *
     * @param email person's email
     *
     * @return natural person or null
     */
    @Depth(2)
    fun findByEmail(email: String): NaturalPerson?

    /**
     * Removes all relationships by natural person id
     *
     * @param id natural person id
     */
    @Query("MATCH (p:NaturalPerson {id: {0}})-[r]->(n) DELETE r")
    fun detach(id: String)
}
