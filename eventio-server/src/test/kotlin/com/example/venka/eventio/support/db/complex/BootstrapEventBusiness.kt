package com.example.venka.eventio.support.db.complex

import com.example.venka.eventio.service.person.LegalPersonService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Bootstrap support class for EventBusiness testing
 */
@Component
class BootstrapEventBusiness(
        val legalPersonRepository: LegalPersonService
) : CommandLineRunner {

    override fun run(vararg args: String) {
        legalPerson.createdEvents.add(event)
        legalPerson.createdEvents.add(event2)

        legalPerson.places.add(place)
        place.rooms.add(room)
        place.rooms.add(room2)

        legalPersonRepository.save(legalPerson)
    }
}
