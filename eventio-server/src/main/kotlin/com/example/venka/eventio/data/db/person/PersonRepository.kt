package com.example.venka.eventio.data.db.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.Person
import org.springframework.data.neo4j.annotation.Depth
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * Repository for {@link Person} entity
 */
@Monitor
interface PersonRepository : Neo4jRepository<Person, String> {

    /**
     * Returns person by email
     *
     * @param email person's email
     *
     * @return person or null
     */
    @Depth(2)
    fun findByEmail(email: String): Person?

    /**
     * Changes NaturalPerson to LegalPerson
     *
     * @param email person's email
     */
    @Query("MATCH (n:Person {email: {0}}) REMOVE n:NaturalPerson SET n:LegalPerson;")
    fun setLegalPersonLabel(email: String)

    /**
     * Changes LegalPerson to NaturalPerson
     *
     * @param email person's email
     */
    @Query("MATCH (n:Person {email: {0}}) REMOVE n:LegalPerson SET n:NaturalPerson;")
    fun setNaturalPersonLabel(email: String)
}
