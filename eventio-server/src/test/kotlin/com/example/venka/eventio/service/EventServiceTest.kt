package com.example.venka.eventio.service

import com.example.venka.eventio.data.db.EventRepository
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.impl.EventServiceImpl
import com.example.venka.eventio.support.db.complex.place
import com.example.venka.eventio.utils.format.formatToString
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.Assert.assertEquals
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.time.LocalDateTime
import java.util.Optional

class EventServiceTest {

    @InjectMockKs
    private lateinit var eventService: EventServiceImpl

    @MockK
    private lateinit var eventRepository: EventRepository

    private val id = "1"

    private val event: Event = Event(id, "hello", location = Room("1"))
    private val event2: Event = Event("2", "hello 2", location = Room("2"))

    private val events = listOf(event, event2)

    private val person = NaturalPerson(id, "hello@gmail.com", "123")

    private val localDateTime = LocalDateTime.now().formatToString()
    private val localDateTime2 = LocalDateTime.now().plusDays(1).formatToString()

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetAll() {
        every { eventRepository.findAll(2) } returns events

        assertEquals(eventService.getAll(), events)
    }

    @Test
    fun testSave() {
        every { eventRepository.save(event, 2) } returns event

        eventService.save(event)
    }

    @Test
    fun testGetById() {
        every { eventRepository.findById(event.id, 2) } returns Optional.of(event)
        every { eventRepository.findById(event2.id, 2) } returns Optional.of(event2)

        assertEquals(eventService.getById(event.id!!), event)
        assertEquals(eventService.getById(event2.id!!), event2)
    }

    @Test
    fun testDeleteById() {
        every { eventRepository.deleteById(event2.id!!) } answers { }

        eventService.deleteById(event2.id!!)
    }

    @Test
    fun testGetByParam() {
        every { eventRepository.findByTitle(event.title) } returns event
        every { eventRepository.findByTitle(event2.title) } returns event2

        assertEquals(eventService.getByParam(event.title), event)
        assertEquals(eventService.getByParam(event2.title), event2)
    }

    @Test
    fun testDeleteAll() {
        every { eventRepository.deleteAll() } answers { }

        eventService.deleteAll()
    }

    @Test
    fun testDeleteAllInvalid() {
        every { eventRepository.deleteAllInvalid() } returns listOf(event.id!!)

        assertEquals(eventService.deleteAllInvalid(), listOf(event.id))
    }

    @Test
    fun testGetOwner() {
        every { eventRepository.findOwner(id) } returns person

        assertEquals(eventService.getOwner(id), person)
    }

    @Test
    fun testSetInvalidLabelByPerson() {
        every { eventRepository.setInvalidLabelByPerson(person.email) } returns listOf(event.id!!, event2.id!!)

        assertEquals(eventService.setInvalidLabelByPerson(person.email), listOf(event.id, event2.id))
    }

    @Test
    fun testSetInvalidLabelByPlace() {
        every { eventRepository.setInvalidLabelByPlace(place.id!!) } returns listOf(event.id!!, event2.id!!)

        assertEquals(eventService.setInvalidLabelByPlace(place.id!!), listOf(event.id, event2.id))
    }

    @Test
    fun testSetInvalidLabelByRoomIds() {
        every { eventRepository.setInvalidLabelByRoomId("1") } returns listOf(event.id!!)
        every { eventRepository.setInvalidLabelByRoomId("2") } returns listOf(event2.id!!)

        assertEquals(eventService.setInvalidLabelByRoomIds(listOf("1", "2")), listOf(event.id, event2.id))
    }

    @Test
    fun testGetSubscribers() {
        every { eventRepository.findSubscribers(event.id!!) } returns listOf(person.email)

        assertEquals(eventService.getSubscribers(event.id!!), listOf(person.email))
    }

    @Test
    fun testIsFree() {
        every { eventRepository.findByLocation(event.id!!) } returns listOf()

        assertTrue(eventService.isFree(event.id!!, localDateTime, localDateTime2))
    }

    @Test
    fun testIsFree_False() {
        every { eventRepository.findByLocation(event2.id!!) } returns listOf(event, event2)

        assertFalse(eventService.isFree(event2.id!!,localDateTime, null))
    }
}
