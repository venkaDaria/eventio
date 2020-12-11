package com.example.venka.eventio.controller.complex

import com.example.venka.eventio.config.security.PersonAuthority.Companion.legal
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.complex.impl.EventBusinessService
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.business.PlaceTranslator
import com.example.venka.eventio.utils.format.formatToString
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.hasSize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.time.LocalDateTime
import java.util.Optional

class EventBusinessControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var eventBusinessController: EventBusinessController

    @MockK
    private lateinit var eventBusinessService: EventBusinessService

    @MockK
    private lateinit var eventTranslator: EventTranslator

    @MockK
    private lateinit var placeTranslator: PlaceTranslator

    private val event = Event("1", "hello", location = Room())
    private val event2 = Event("2", "hello 2", location = Room())

    private val eventDto = EventDto("1", "hello", location = RoomDto())
    private val eventDto2 = EventDto("2", "hello 2", location = RoomDto())

    private val localDateTime = LocalDateTime.now()
    private val localDateTime2 = localDateTime.plusDays(1)

    private val principal = UsernamePasswordAuthenticationToken(UserAuthenticationProvider.PersonDetails("hello@gmail.com"),
            "password", legal)

    private val place = Place(realAddress = "hello")
    private val placeDto = PlaceDto(realAddress = "hello")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = eventBusinessController.createMockMvc()

        every { eventTranslator.toDto(event) } returns eventDto
        every { eventTranslator.toDto(event2) } returns eventDto2
    }

    @Test
    fun testFindAllByPlaceId() {
        every { eventBusinessService.findAllByPlaceId("1") } returns listOf(event, event2)

        val result = mockMvc.perform(get("/event/places/1"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(eventDto, eventDto2))
    }

    @Test
    fun testFindAllByRoomId() {
        every { eventBusinessService.findAllByRoomId("1") } returns listOf(event, event2)

        val result = mockMvc.perform(get("/event/rooms/1"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(eventDto, eventDto2))
    }

    @Test
    fun testFindAllBetweenDates() {
        every {
            eventBusinessService.findAllBetweenDates(principal, localDateTime.formatToString(),
                    Optional.of(localDateTime2.formatToString()), true)
        } returns listOf(place)
        every { placeTranslator.toDto(place) } returns placeDto

        val result = mockMvc.perform(get("/event/places")
                .param("start", localDateTime.formatToString())
                .param("end", localDateTime2.formatToString())
                .param("onlyMine", "true")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(1)))
                .andReturn()

        result.assertEquals(listOf(placeDto))
    }
}
