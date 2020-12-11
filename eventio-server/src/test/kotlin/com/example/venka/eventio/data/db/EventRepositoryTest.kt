package com.example.venka.eventio.data.db

import com.example.venka.eventio.data.db.person.PersonRepository
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.service.impl.EventServiceImpl
import com.example.venka.eventio.support.assertEqualsCollection
import com.example.venka.eventio.support.db.BootstrapEvent
import com.example.venka.eventio.support.db.event
import com.example.venka.eventio.support.db.event2
import com.example.venka.eventio.support.db.eventInvalid
import com.example.venka.eventio.support.db.person2
import com.example.venka.eventio.support.db.place
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@DataNeo4jTest
class EventRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var personRepository: PersonRepository

    private lateinit var eventService: EventServiceImpl

    private val event3: Event = Event("3", "hello 3")

    private val events = listOf(event, event2, eventInvalid)

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapEvent(eventRepository, personRepository)
        bootstrapPerson.run()

        eventService = EventServiceImpl(eventRepository)
    }

    @Test
    fun testGetAll() {
        eventService.getAll().assertEqualsCollection<Event, String>(events)
    }

    @Test
    fun testSave() {
        eventService.save(event3)

        eventService.getAll().assertEqualsCollection<Event, String>(events + event3)
    }

    @Test
    fun testGetById() {
        assertEquals(eventService.getById(event.id!!), event)
        assertEquals(eventService.getById(event2.id!!), event2)
    }

    @Test
    fun testDeleteById() {
        eventService.deleteById(event2.id!!)

        eventService.getAll().assertEqualsCollection<Event, String>(listOf(event, eventInvalid))
    }

    @Test
    fun testGetByParam() {
        assertEquals(eventService.getByParam(event.title), event)
        assertEquals(eventService.getByParam(event2.title), event2)
    }

    @Test
    fun testDeleteAll() {
        eventService.deleteAll()

        assertEquals(eventService.getAll(), emptyList<Event>())
    }

    @Test
    fun testDeleteAllInvalid() {
        assertEquals(eventService.deleteAllInvalid(), listOf(eventInvalid.id))
    }

    @Test
    fun testGetOwner() {
        assertEquals(eventService.getOwner(event.id!!).email, person2.email)
    }

    @Test
    fun testSetInvalidLabelByPerson() {
        eventService.setInvalidLabelByPerson(person2.email)

        assertEquals(eventService.deleteAllInvalid(), listOf(event.id, eventInvalid.id))
    }

    @Test
    fun testSetInvalidLabelByPlace() {
        eventService.setInvalidLabelByPlace(place.id!!)

        assertEquals(eventService.deleteAllInvalid(), listOf(event2.id, eventInvalid.id))
    }
}
