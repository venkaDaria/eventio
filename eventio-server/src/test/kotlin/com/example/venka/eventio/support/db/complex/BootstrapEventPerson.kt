package com.example.venka.eventio.support.db.complex

import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.service.person.NaturalPersonService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Bootstrap support class for EventPerson testing
 */
@Component
class BootstrapEventPerson(
        val naturalPersonService: NaturalPersonService,
        val legalPersonService: LegalPersonService
) : CommandLineRunner {

    override fun run(vararg args: String) {
        legalPerson.createdEvents.add(event)
        legalPerson.createdEvents.add(event2)

        naturalPerson.subscribedEvents.add(event)
        naturalPerson.subscribedEvents.add(event2)

        naturalPerson2.subscribedEvents.add(event)

        legalPerson.places.add(place)
        place.rooms.add(room)
        place.rooms.add(room2)

        legalPersonService.save(legalPerson)
        naturalPersonService.save(naturalPerson)
        naturalPersonService.save(naturalPerson2)
    }
}
