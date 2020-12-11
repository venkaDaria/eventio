package com.example.venka.eventio.service.complex

import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.business.PlaceService
import com.example.venka.eventio.service.business.RoomService
import com.example.venka.eventio.service.complex.impl.EventBusinessService
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.utils.format.formatToString
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.security.Principal
import java.time.LocalDateTime
import java.util.Optional

class EventBusinessServiceTest {

    @InjectMockKs
    private lateinit var eventBusinessService: EventBusinessService

    @MockK
    private lateinit var eventService: EventService

    @MockK
    private lateinit var roomService: RoomService

    @MockK
    private lateinit var placeService: PlaceService

    @MockK
    private lateinit var legalPersonService: LegalPersonService

    private val id = "1"
    private val id2 = "2"

    private val place = Place(id = id, realAddress = "my place")

    private val room = Room(id = id2, name = "room")

    private val room2 = Room(id = "3", name = "room2")

    private val room3 = Room(id = "4", name = "room3")

    private val helloEmail = "hello@gmail.com"

    private val legalPerson = LegalPerson("3", helloEmail, "789")

    private val event = Event(id, "hello", location = room, end = LocalDateTime.now().plusHours(3))
    private val event2 = Event(id2, "hello 2", location = room2)
    private val event3 = Event(id, "hello", location = room3, end = LocalDateTime.now().plusHours(28))

    private val localDateTime = LocalDateTime.now().plusDays(1)
    private val localDateTime2 = localDateTime.plusDays(2)

    private val principal = UserAuthenticationProvider.PersonDetails(helloEmail)

    private val principal2 = BadPrincipalDetails()

    private class BadPrincipalDetails : Principal {
        override fun getName(): String {
            return "wrong"
        }
    }

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        legalPerson.createdEvents.add(event)
        legalPerson.createdEvents.add(event2)

        legalPerson.places.add(place)
        place.rooms.add(room)
    }

    @Test
    fun testFindGetAllByPlaceId() {
        every { placeService.getById(id) } returns place
        every { eventService.getAll() } returns listOf(event, event2)

        val findAllEvents = eventBusinessService.findAllByPlaceId(id)
        findAllEvents.assertEquals(event)
    }

    @Test
    fun testFindGetAllByPlaceId_Empty() {
        every { placeService.getById("wrong id") } returns null

        val findAllEvents = eventBusinessService.findAllByPlaceId("wrong id")
        findAllEvents.assertEquals(emptyList())
    }

    @Test
    fun testFindGetAllByRoomId() {
        every { roomService.getById(id2) } returns room
        every { eventService.getAll() } returns listOf(event, event2)

        val findAllEvents = eventBusinessService.findAllByRoomId(id2)
        findAllEvents.assertEquals(event)
    }

    @Test
    fun testFindGetAllByRoomId_Empty() {
        every { roomService.getById("wrong id") } returns null


        val findAllEvents = eventBusinessService.findAllByRoomId("wrong id")
        findAllEvents.assertEquals(emptyList())
    }

    @Test
    fun testFindAllBetweenDates() {
        every { eventService.getAll() } returns listOf(event, event2, event3)
        every { placeService.getAll() } returns listOf(place)
        every { legalPersonService.getByParam(helloEmail) } returns legalPerson

        val findPlaces = eventBusinessService.findAllBetweenDates(principal, localDateTime.formatToString(),
                Optional.of(localDateTime2.formatToString()), false)
        findPlaces.assertEquals(listOf(place))
    }

    @Test
    fun testFindAllBetweenDates_OnlyMine() {
        every { eventService.getAll() } returns listOf(event, event2, event3)
        every { placeService.getAll() } returns listOf(place)
        every { legalPersonService.getByParam(helloEmail) } returns legalPerson

        val findPlaces = eventBusinessService.findAllBetweenDates(principal, localDateTime.formatToString(),
                Optional.of(localDateTime2.formatToString()), true)
        findPlaces.assertEquals(listOf(place))
    }

    @Test(expectedExceptions = [ClassCastException::class])
    fun testFindAllBetweenDates_NotDetails() {
        every { eventService.getAll() } returns listOf(event, event2, event3)
        every { placeService.getAll() } returns listOf(place)

        eventBusinessService.findAllBetweenDates(principal2, localDateTime.formatToString(),
                Optional.of(localDateTime2.formatToString()), true)
    }

    @Test
    fun testFindAllBetweenDates_OnlyMine_WithNullFinishTime() {
        every { eventService.getAll() } returns listOf(event, event2, event3)
        every { placeService.getAll() } returns listOf(place)
        every { legalPersonService.getByParam(helloEmail) } returns legalPerson

        val findPlaces = eventBusinessService.findAllBetweenDates(principal, localDateTime.formatToString(),
                Optional.empty(), true)
        findPlaces.assertEquals(listOf(place))
    }
}
