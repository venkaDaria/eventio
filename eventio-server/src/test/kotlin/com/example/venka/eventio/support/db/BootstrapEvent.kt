package com.example.venka.eventio.support.db

import com.example.venka.eventio.data.db.EventRepository
import com.example.venka.eventio.data.db.person.PersonRepository
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.support.db.person.person
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

val room = Room("1", "hello")

val place = Place("1", "Hello", "Blue street", rooms = mutableSetOf(room))

val event = Event("1", "hello")

val event2 = Event("2", "hello 2", location = room)

val eventInvalid = Event("inv", "hello inv")

val person = NaturalPerson("1", "hello@gmail.com", "123")

val person2 = LegalPerson("2", "hello2@gmail.com", "456", createdEvents = mutableSetOf(event),
        places = mutableSetOf(place))

/**
 * Bootstrap support class for Event testing
 */
@Component
class BootstrapEvent(
        private val eventRepository: EventRepository,
        private val personRepository: PersonRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        eventRepository.save(event)
        eventRepository.save(event2)

        eventRepository.save(eventInvalid)
        eventRepository.setInvalidLabel(eventInvalid.id!!)

        personRepository.saveAll(listOf(person, person2))
    }
}
