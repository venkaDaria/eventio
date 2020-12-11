package com.example.venka.eventio.data.db.complex

import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.business.PlaceService
import com.example.venka.eventio.service.business.RoomService
import com.example.venka.eventio.service.business.impl.PlaceServiceImpl
import com.example.venka.eventio.service.business.impl.RoomServiceImpl
import com.example.venka.eventio.service.complex.impl.EventBusinessService
import com.example.venka.eventio.service.impl.EventServiceImpl
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.db.complex.BootstrapEventBusiness
import com.example.venka.eventio.support.db.complex.event
import com.example.venka.eventio.support.db.complex.event2
import com.example.venka.eventio.support.db.complex.helloEmail
import com.example.venka.eventio.support.db.complex.id
import com.example.venka.eventio.support.db.complex.id2
import com.example.venka.eventio.support.db.complex.place
import com.example.venka.eventio.utils.format.formatToString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.security.Principal
import java.time.LocalDateTime
import java.util.Optional

@DataNeo4jTest
@Import(
        EventServiceImpl::class,
        RoomServiceImpl::class,
        PlaceServiceImpl::class,
        LegalPersonServiceImpl::class
)
class EventBusinessRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var eventService: EventService

    @Autowired
    private lateinit var roomService: RoomService

    @Autowired
    private lateinit var placeService: PlaceService

    @Autowired
    private lateinit var legalPersonService: LegalPersonService

    private lateinit var eventBusinessService: EventBusinessService

    private val localDateTime: LocalDateTime = LocalDateTime.now().plusDays(1)
    private val localDateTime2: LocalDateTime = localDateTime.plusDays(2)

    private val principal = UserAuthenticationProvider.PersonDetails(helloEmail)

    private val principal2 = BadPrincipalDetails()

    class BadPrincipalDetails : Principal {
        override fun getName(): String {
            return "wrong"
        }
    }

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapEventBusiness(legalPersonService)
        bootstrapPerson.run()

        eventBusinessService = EventBusinessService(legalPersonService, roomService, placeService, eventService)
    }

    @Test
    fun testFindGetAllByPlaceId() {
        val findAllEvents = eventBusinessService.findAllByPlaceId(id)
        findAllEvents.assertEquals(event, event2)
    }

    @Test
    fun testFindGetAllByPlaceId_Empty() {
        val findAllEvents = eventBusinessService.findAllByPlaceId("wrong id")
        Assert.assertEquals(findAllEvents, emptyList<Event>())
    }

    @Test
    fun testFindGetAllByRoomId() {
        val findAllEvents = eventBusinessService.findAllByRoomId(id2)
        findAllEvents.assertEquals(event)
    }

    @Test
    fun testFindGetAllByRoomId_Empty() {
        val findAllEvents = eventBusinessService.findAllByRoomId("wrong id")
        Assert.assertEquals(findAllEvents, emptyList<Event>())
    }

    @Test
    fun testFindAllBetweenDates() {
        val findPlaces = eventBusinessService.findAllBetweenDates(principal, localDateTime.formatToString(),
                Optional.of(localDateTime2.formatToString()), false)
        findPlaces.assertEquals(place)
    }

    @Test
    fun testFindAllBetweenDates_OnlyMine() {
        val findPlaces = eventBusinessService.findAllBetweenDates(principal, localDateTime.formatToString(),
                Optional.of(localDateTime2.formatToString()), true)
        findPlaces.assertEquals(place)
    }

    @Test(expectedExceptions = [ClassCastException::class])
    fun testFindAllBetweenDates_NotDetails() {
        eventBusinessService.findAllBetweenDates(principal2, localDateTime.formatToString(),
                Optional.of(localDateTime2.formatToString()), true)
    }

    @Test
    fun testFindAllBetweenDates_OnlyMine_WithNullFinishTime() {
        val findPlaces = eventBusinessService.findAllBetweenDates(principal, localDateTime.formatToString(),
                Optional.empty(), true)
        findPlaces.assertEquals(place)
    }
}
