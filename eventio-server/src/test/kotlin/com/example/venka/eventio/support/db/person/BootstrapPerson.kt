package com.example.venka.eventio.support.db.person

import com.example.venka.eventio.data.db.person.PersonRepository
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

val person = NaturalPerson("1", "hello@gmail.com", "123")

val person2 = LegalPerson("2", "hello_2@gmail.com", "456")

/**
 * Bootstrap support class for Person testing
 */
@Component
class BootstrapPerson(private val personRepository: PersonRepository) : CommandLineRunner {

    override fun run(vararg args: String) {
        personRepository.save(person)
        personRepository.save(person2)
    }
}
