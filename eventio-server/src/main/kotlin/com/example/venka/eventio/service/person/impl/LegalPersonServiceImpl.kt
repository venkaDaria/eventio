package com.example.venka.eventio.service.person.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.db.person.LegalPersonRepository
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.service.AbstractService
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.utils.getUrlString
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Service

/**
 * Implementation of {@link LegalPersonService}
 */
@Monitor
@Service
class LegalPersonServiceImpl(neo4jRepository: Neo4jRepository<LegalPerson, String>)
    : AbstractService<LegalPerson, String, String>(neo4jRepository), LegalPersonService {

    private val legalPersonRepository = neo4jRepository as LegalPersonRepository

    override fun getByParam(param: String) = legalPersonRepository.findByEmail(param)

    override fun getByCompanyUrl(url: String) = legalPersonRepository.findByUrl(url)

    override fun fixCompanyUrl(legalPerson: LegalPerson): LegalPerson {
        if (legalPerson.name.isBlank()) {
            legalPerson.name = getUrlString()
        }

        val oldUrl = legalPerson.url

        while (true) {
            val isValid = legalPersonRepository.findByUrl(legalPerson.url) == null
            if (isValid) {
                return legalPerson
            }

            val randomString = getUrlString()
            legalPerson.url = "$oldUrl-$randomString"
        }
    }
}
