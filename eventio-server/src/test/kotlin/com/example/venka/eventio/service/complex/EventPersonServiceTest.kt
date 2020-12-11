package com.example.venka.eventio.service.complex

import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.service.complex.impl.EventPersonService
import com.example.venka.eventio.service.impl.EventServiceImpl
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.NaturalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.PersonServiceImpl
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.assertEqualsCollection
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class EventPersonServiceTest {

    @InjectMockKs
    private lateinit var eventPersonService: EventPersonService

    @MockK
    private lateinit var naturalPersonService: NaturalPersonServiceImpl

    @MockK
    private lateinit var legalPersonService: LegalPersonServiceImpl

    @MockK
    private lateinit var personService: PersonServiceImpl

    @MockK
    private lateinit var eventService: EventServiceImpl

    private val id = "1"
    private val id2 = "2"

    private val place = Place(id = id, realAddress = "my place")

    private val room = Room(id = id2, name = "room")

    private val naturalPerson = NaturalPerson(id, "hello@gmail.com", "123")
    private val naturalPerson2 = NaturalPerson("2", "hello2@gmail.com", "456")
    private val legalPerson = LegalPerson("3", "hello3@gmail.com", "789")

    private val event = Event(id, "hello", location = room)
    private val event2 = Event(id2, "hello 2", location = room)

    private val eventsDto = mapOf(event to 2, event2 to 1)

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        legalPerson.createdEvents.add(event)
        legalPerson.createdEvents.add(event2)

        naturalPerson.subscribedEvents.add(event)
        naturalPerson.subscribedEvents.add(event2)

        naturalPerson2.subscribedEvents.add(event)

        legalPerson.places.add(place)
        place.rooms.add(room)
    }

    @Test
    fun testFindAllEvents() {
        every { legalPersonService.getByParam(legalPerson.email) } returns legalPerson
        every { naturalPersonService.getAll() } returns listOf(naturalPerson, naturalPerson2)
        every { eventService.getAll() } returns listOf(event, event2)

        val findAllEvents = eventPersonService.findAllEvents(legalPerson.email)
        findAllEvents.assertEqualsCollection<Event, String>(eventsDto)
    }

    @Test
    fun testFindAllEvents_Null() {
        every { legalPersonService.getByParam(legalPerson.email) } returns null

        val findAllEvents = eventPersonService.findAllEvents(legalPerson.email)
        assertEquals(findAllEvents, emptyMap<Event, Int>())
    }

    @Test
    fun testFindAllCreatedEvents() {
        every { personService.getByParam(legalPerson.email) } returns legalPerson
        every { naturalPersonService.getAll() } returns listOf(naturalPerson, naturalPerson2)

        val findAllEvents = eventPersonService.findAllCreatedEvents(legalPerson.email)
        findAllEvents.assertEqualsCollection<Event, String>(eventsDto)
    }

    @Test
    fun testFindAllCreatedEvents_Null() {
        every { personService.getByParam(legalPerson.email) } returns null

        val findAllEvents = eventPersonService.findAllCreatedEvents(legalPerson.email)
        assertEquals(findAllEvents, emptyMap<Event, Int>())
    }

    @Test
    fun testFindAllSubscribedEvents() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns naturalPerson
        every { naturalPersonService.getAll() } returns listOf(naturalPerson, naturalPerson2)

        val findAllEvents = eventPersonService.findAllSubscribedEvents(naturalPerson.email)
        findAllEvents.assertEqualsCollection<Event, String>(eventsDto)
    }

    @Test
    fun testFindAllSubscribedEvents_Null() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns null

        val findAllEvents = eventPersonService.findAllSubscribedEvents(naturalPerson.email)
        assertEquals(findAllEvents, emptyMap<Event, Int>())
    }

    @Test
    fun testSubscribe() {
        every { eventService.getById(id) } returns event
        every { personService.getByParam(naturalPerson.email) } returns naturalPerson
        every { personService.save(any()) } returns naturalPerson
        every { naturalPersonService.save(naturalPerson) } returns naturalPerson

        val actual = eventPersonService.subscribe(id, naturalPerson.email)
        actual.assertEquals(naturalPerson)
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testSubscribe_Null() {
        every { eventService.getById(id) } returns event
        every { personService.getByParam(naturalPerson.email) } returns null

        eventPersonService.subscribe(id, naturalPerson.email)
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testSubscribe_LegalPerson() {
        every { eventService.getById(id) } returns event
        every { personService.getByParam(legalPerson.email) } returns legalPerson

        eventPersonService.subscribe(id, legalPerson.email)
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testSubscribe_NullEvent() {
        every { eventService.getById(id) } returns null

        eventPersonService.subscribe(id, naturalPerson.email)
    }

    @Test
    fun testUnsubscribe() {
        every { eventService.getById(id) } returns event
        every { naturalPersonService.getByParam(naturalPerson.email) } returns naturalPerson
        every { naturalPersonService.detach(naturalPerson.id!!) } answers { }
        every { personService.save(any()) } returns naturalPerson
        every { naturalPersonService.save(naturalPerson) } returns naturalPerson

        val actual = eventPersonService.unsubscribe(id, naturalPerson.email)
        actual.assertEquals(naturalPerson)
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testUnsubscribe_Null() {
        every { eventService.getById(id) } returns event
        every { naturalPersonService.getByParam(naturalPerson.email) } returns null

        eventPersonService.unsubscribe(id, naturalPerson.email)
    }

    @Test(expectedExceptions = [NotFoundException::class])
    fun testUnsubscribe_NullEvent() {
        every { eventService.getById(id) } returns null

        eventPersonService.unsubscribe(id, naturalPerson.email)
    }

    @Test
    fun testGetOwner() {
        every { eventService.getOwner(id) } returns legalPerson

        val owner = eventPersonService.getOwner(id)
        owner.assertEquals(legalPerson)
    }

    @Test
    fun testCreate() {
        every { personService.getByParam(naturalPerson.email) } returns naturalPerson
        every { personService.save(any()) } returns naturalPerson
        every { naturalPersonService.save(naturalPerson) } returns naturalPerson

        val actual = eventPersonService.create(event, naturalPerson.email)
        actual!!.assertEquals(naturalPerson)
    }
}