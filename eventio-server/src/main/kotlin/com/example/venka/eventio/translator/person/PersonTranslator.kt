package com.example.venka.eventio.translator.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.person.PersonDto
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.Translator
import org.springframework.stereotype.Service

/**
 * Person Translator
 */
@Monitor
@Service
class PersonTranslator(private val eventTranslator: EventTranslator) : Translator<Person, PersonDto, String> {

    override fun fromDto(source: PersonDto, id: String?): Person {
        return NaturalPerson(
                id,
                source.email,
                createdEvents = source.createdEvents.map { eventTranslator.fromDto(it) }.toMutableSet(),
                subscribedEvents = source.subscribedEvents.map { eventTranslator.fromDto(it) }.toMutableSet()
        )
    }

    override fun toDto(source: Person): PersonDto {
        return PersonDto(
                source.email,
                source.createdEvents.map { eventTranslator.toDto(it) }.toMutableSet(),
                getSubscribedEvents(source).map { eventTranslator.toDto(it) }.toMutableSet()
        )
    }

    private fun getSubscribedEvents(source: Person): MutableSet<Event> =
            (source as? NaturalPerson)?.subscribedEvents ?: mutableSetOf()
}
