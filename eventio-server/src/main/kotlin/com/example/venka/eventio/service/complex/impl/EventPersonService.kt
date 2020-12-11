package com.example.venka.eventio.service.complex.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.service.person.NaturalPersonService
import com.example.venka.eventio.service.person.PersonService
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.stereotype.Service

/**
 * Service for complex operations between person and events
 */
@Monitor
@Service
class EventPersonService(
        private val personService: PersonService,
        private val naturalPersonService: NaturalPersonService,
        private val legalPersonService: LegalPersonService,
        private val eventService: EventService
) : Logging by LoggingImpl<EventPersonService>() {

    /**
     * Creates map where keys are events created by person and values are count of subscribed people
     *
     * @param email Person email
     *
     * @return map event to count
     */
    fun findAllCreatedEvents(email: String): Map<Event, Int> {
        val person = personService.getByParam(email) ?: return emptyMap()
        log.debug("finds person: $person with email=$email")

        val persons = naturalPersonService.getAll()
        log.trace("finds all natural persons: $persons")

        return person.createdEvents.map { it to count(persons, it) }.toMap()
    }

    /**
     * Creates map where keys are events subscribed by person and values are count of subscribed people
     *
     * @param email Person email
     *
     * @return map event to count
     */
    fun findAllSubscribedEvents(email: String): Map<Event, Int> {
        val person = naturalPersonService.getByParam(email) ?: return emptyMap()
        log.debug("finds person: $person with email=$email")

        val persons = naturalPersonService.getAll()
        log.trace("finds all natural persons: $persons")

        return person.subscribedEvents.map { it to count(persons, it) }.toMap()
    }

    /**
     * Creates map where keys are events located in places created by person and values are count of subscribed people
     *
     * @param email Person email
     *
     * @return map event to count
     */
    fun findAllEvents(email: String): Map<Event, Int> {
        val person = legalPersonService.getByParam(email) ?: return emptyMap()
        log.debug("finds person: $person with email=$email")

        val events = eventService.getAll()
        log.trace("finds all events: $events")

        val filteredEvents = events.filter { event -> person.places.any { event.location in it.rooms } }
        log.trace("filtered events: $events")

        val persons = naturalPersonService.getAll()
        log.trace("finds all natural persons: $persons")

        return filteredEvents.map { it to count(persons, it) }.toMap()
    }

    private fun count(persons: List<NaturalPerson>, event: Event): Int {
        return persons
                .map { it.subscribedEvents }
                .filter { it.any { event == it } }
                .count()
    }

    /**
     * Subscribes to an event
     *
     * @param id Event id
     * @param email Person email
     *
     * @return subscribed NaturalPerson
     */
    fun subscribe(id: String, email: String): NaturalPerson {
        val event = eventService.getById(id) ?: throw NotFoundException()

        return subscribeWith(event, email) as? NaturalPerson ?: throw NotFoundException()
    }

    /**
     * Unsubscribes from an event
     *
     * @param id Event id
     * @param email Person email
     *
     * @return unsubscribed NaturalPerson
     */
    fun unsubscribe(id: String, email: String): NaturalPerson {
        val event = eventService.getById(id) ?: throw NotFoundException()
        log.debug("finds event: $event with id=$id")

        val person = naturalPersonService.getByParam(email) ?: throw NotFoundException()
        log.debug("finds person: $person with email=$email")

        naturalPersonService.detach(person.id!!) // to remove relationships

        person.subscribedEvents.remove(event)
        naturalPersonService.save(person)

        log.debug("unsubscribed to event: $person")

        return person
    }

    /**
     * Returns who created an event by event id
     *
     * @param id event id
     *
     * @return person created this event
     */
    fun getOwner(id: String): Person = eventService.getOwner(id)

    /**
     * Creates an event for a person
     *
     * @param event Event created
     * @param email Person email
     *
     * @return subscribed NaturalPerson
     */
    fun create(event: Event, email: String): Person? = subscribeWith(event, email) {
        person, it -> person.createdEvents.add(it)
    }

    private val emptyFunc = { _: Person, _: Event -> }

    private fun subscribeWith(event: Event, email: String, also: (person: Person, event: Event) -> Unit = emptyFunc)
            : Person? {
        log.debug("finds event: $event with id=${event.id}")

        val person = personService.getByParam(email) ?: throw NotFoundException()
        log.debug("finds person: $person with email=$email")

        also(person, event) // also do something

        (person as? NaturalPerson)?.subscribedEvents?.add(event)

        personService.save(person)

        log.debug("subscribed to event: $person")

        return person
    }
}
