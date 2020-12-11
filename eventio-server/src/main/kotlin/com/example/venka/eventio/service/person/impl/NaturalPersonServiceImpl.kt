package com.example.venka.eventio.service.person.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.db.person.NaturalPersonRepository
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.service.AbstractService
import com.example.venka.eventio.service.person.NaturalPersonService
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Service

/**
 * Implementation of {@link NaturalPersonService}
 */
@Monitor
@Service
class NaturalPersonServiceImpl(neo4jRepository: Neo4jRepository<NaturalPerson, String>)
    : AbstractService<NaturalPerson, String, String>(neo4jRepository), NaturalPersonService {

    private val naturalPersonRepository = neo4jRepository as NaturalPersonRepository

    override fun getByParam(param: String): NaturalPerson? = naturalPersonRepository.findByEmail(param)

    override fun detach(id: String) = naturalPersonRepository.detach(id)
}
