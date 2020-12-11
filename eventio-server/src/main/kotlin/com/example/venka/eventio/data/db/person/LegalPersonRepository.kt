package com.example.venka.eventio.data.db.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.LegalPerson
import org.springframework.data.neo4j.annotation.Depth
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * Repository for {@link LegalPerson} entity
 */
@Monitor
interface LegalPersonRepository : Neo4jRepository<LegalPerson, String> {

    /**
     * Returns legal person by email
     *
     * @param email person's email
     *
     * @return legal person or null
     */
    @Depth(2)
    fun findByEmail(email: String): LegalPerson?

    /**
     * Returns legal person by email
     *
     * @param url company url
     *
     * @return legal person or null
     */
    @Depth(2)
    fun findByUrl(url: String): LegalPerson?
}
