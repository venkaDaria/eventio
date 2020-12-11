package com.example.venka.eventio.support.db.person

import com.example.venka.eventio.data.db.person.NaturalPersonRepository
import com.example.venka.eventio.data.model.NaturalPerson
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

val naturalPerson: NaturalPerson = NaturalPerson("1", "hello@gmail.com", "123")

val naturalPerson2: NaturalPerson = NaturalPerson("2", "hello_2@gmail.com", "456")

/**
 * Bootstrap support class for NaturalPerson testing
 */
@Component
class BootstrapNaturalPerson(private val naturalPersonRepository: NaturalPersonRepository) : CommandLineRunner {

    override fun run(vararg args: String) {
        naturalPersonRepository.save(naturalPerson)
        naturalPersonRepository.save(naturalPerson2)
    }
}
