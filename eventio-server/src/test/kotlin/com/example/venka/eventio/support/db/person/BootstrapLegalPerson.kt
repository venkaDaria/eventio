package com.example.venka.eventio.support.db.person

import com.example.venka.eventio.data.db.person.LegalPersonRepository
import com.example.venka.eventio.data.model.LegalPerson
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

val legalPerson: LegalPerson = LegalPerson("1", "hello@gmail.com", "123")

val legalPerson2: LegalPerson = LegalPerson("2", "hello_2@gmail.com", "456")

/**
 * Bootstrap support class for LegalPerson testing
 */
@Component
class BootstrapLegalPerson(private val legalPersonRepository: LegalPersonRepository) : CommandLineRunner {

    override fun run(vararg args: String) {
        legalPersonRepository.save(legalPerson)
        legalPersonRepository.save(legalPerson2)
    }
}
