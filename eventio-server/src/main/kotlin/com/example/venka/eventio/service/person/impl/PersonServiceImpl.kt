package com.example.venka.eventio.service.person.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.config.security.PersonAuthority.Companion.legal
import com.example.venka.eventio.data.db.person.PersonRepository
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.service.AbstractService
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.service.person.NaturalPersonService
import com.example.venka.eventio.service.person.PersonService
import com.example.venka.eventio.utils.changeRoles
import com.example.venka.eventio.utils.eq
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

/**
 * Implementation of {@link PersonService}
 */
@Monitor
@Service
class PersonServiceImpl(
        neo4jRepository: Neo4jRepository<Person, String>,
        private val legalPersonService: LegalPersonService,
        private val naturalPersonService: NaturalPersonService
) : AbstractService<Person, String, String>(neo4jRepository), PersonService {
    private val personRepository = neo4jRepository as PersonRepository

    override fun getByParam(param: String): Person? =
            naturalPersonService.getByParam(param) ?: legalPersonService.getByParam(param)

    override fun getById(id: String): Person? =
            naturalPersonService.getById(id) ?: legalPersonService.getById(id)

    override fun deleteById(id: String) {
        naturalPersonService.deleteById(id)
        legalPersonService.deleteById(id)
    }

    override fun toggleLabel(authentication: Authentication) {
        val isCompany = authentication.authorities eq legal

        log.debug("getLabel label with isCompany=$isCompany and email=${authentication.name}")
        authentication.changeRoles(isCompany)

        val email = authentication.name
        if (isCompany) {
            log.debug("change LegalPerson to NaturalPerson")
            personRepository.setNaturalPersonLabel(email)
        } else {
            log.debug("change NaturalPerson to LegalPerson")
            personRepository.setLegalPersonLabel(email)
        }
    }
}
