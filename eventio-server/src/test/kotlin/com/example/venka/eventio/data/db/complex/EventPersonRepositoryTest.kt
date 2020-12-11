package com.example.venka.eventio.data.db.complex

import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.complex.impl.EventPersonService
import com.example.venka.eventio.service.impl.EventServiceImpl
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.service.person.NaturalPersonService
import com.example.venka.eventio.service.person.PersonService
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.NaturalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.PersonServiceImpl
import com.example.venka.eventio.support.assertEqualsCollection
import com.example.venka.eventio.support.db.complex.BootstrapEventPerson
import com.example.venka.eventio.support.db.complex.event
import com.example.venka.eventio.support.db.complex.event2
import com.example.venka.eventio.support.db.complex.id
import com.example.venka.eventio.support.db.complex.id2
import com.example.venka.eventio.support.db.complex.legalPerson
import com.example.venka.eventio.support.db.complex.naturalPerson
import com.example.venka.eventio.support.db.complex.naturalPerson2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@DataNeo4jTest
@Import(
        EventServiceImpl::class,
        NaturalPersonServiceImpl::class,
        LegalPersonServiceImpl::class,
        PersonServiceImpl::class
)
class EventPersonRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var naturalPersonService: NaturalPersonService

    @Autowired
    private lateinit var legalPersonService: LegalPersonService

    @Autowired
    private lateinit var personService: PersonService

    @Autowired
    private lateinit var eventService: EventService

    private lateinit var eventPersonService: EventPersonService

    private val eventsDto = mapOf(event to 2, event2 to 1)

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapEventPerson(naturalPersonService, legalPersonService)
        bootstrapPerson.run()

        eventPersonService = EventPersonService(personService, naturalPersonService, legalPersonService, eventService)
    }

    @Test
    fun testFindAllEvents() {
        val findAllEvents = eventPersonService.findAllEvents(legalPerson.email)
        findAllEvents.assertEqualsCollection<Event, String>(eventsDto)
    }

    @Test
    fun testFindAllEvents_Null() {
        val findAllEvents = eventPersonService.findAllEvents("wrong email")
        assertEquals(findAllEvents, emptyMap<Event, Int>())
    }

    @Test
    fun testFindAllCreatedEvents() {
        val findAllEvents = eventPersonService.findAllCreatedEvents(legalPerson.email)
        findAllEvents.assertEqualsCollection<Event, String>(eventsDto)
    }

    @Test
    fun testFindAllCreatedEvents_Null() {
        val findAllEvents = eventPersonService.findAllCreatedEvents("wrong email")
        assertEquals(findAllEvents, emptyMap<Event, Int>())
    }

    @Test
    fun testFindAllSubscribedEvents() {
        val findAllEvents = eventPersonService.findAllSubscribedEvents(naturalPerson.email)
        findAllEvents.assertEqualsCollection<Event, String>(eventsDto)
    }

    @Test
    fun testFindAllSubscribedEvents_Null() {
        val findAllEvents = eventPersonService.findAllSubscribedEvents("wrong email")
        assertEquals(findAllEvents, emptyMap<Event, Int>())
    }

    @Test
    fun testSubscribe() {
        eventPersonService.subscribe(id2, naturalPerson2.email)

        assertTrue(naturalPersonService.getByParam(naturalPerson2.email)!!.subscribedEvents.any {
            it.id == id2
        })
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testSubscribe_Null() {
        eventPersonService.subscribe(id, "wrong email")
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testSubscribe_NullEvent() {
        eventPersonService.subscribe("4", naturalPerson.email)
    }

    @Test
    fun testUnsubscribe() {
        eventPersonService.unsubscribe(id, naturalPerson.email)

        assertTrue(naturalPersonService.getByParam(naturalPerson.email)!!.subscribedEvents.none {
            it.id == id
        })
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testUnsubscribe_Null() {
        eventPersonService.unsubscribe(id, "wrong email")
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testUnsubscribe_NullEvent() {
        eventPersonService.unsubscribe("4", naturalPerson.email)
    }

    @Test
    fun testGetOwner() {
        val owner = eventPersonService.getOwner(id)

        assertEquals(owner.email, legalPerson.email)
    }
}