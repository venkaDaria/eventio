package com.example.venka.eventio.controller.complex

import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.dto.person.NaturalPersonDto
import com.example.venka.eventio.data.dto.person.PersonDto
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.complex.impl.EventPersonService
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.person.NaturalPersonTranslator
import com.example.venka.eventio.translator.person.PersonTranslator
import com.example.venka.eventio.utils.format.translateMap
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.hasSize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class EventPersonControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var eventPersonController: EventPersonController

    @MockK
    private lateinit var eventPersonService: EventPersonService

    @MockK
    private lateinit var naturalPersonTranslator: NaturalPersonTranslator

    @MockK
    private lateinit var personTranslator: PersonTranslator

    @MockK
    private lateinit var eventTranslator: EventTranslator

    private val event = Event("1", "hello", location = Room())
    private val event2 = Event("2", "hello 2", location = Room())

    private val eventDto = EventDto("1", "hello", location = RoomDto())
    private val eventDto2 = EventDto("2", "hello 2", location = RoomDto())

    private val eventsDto: Map<Event, Int> = mapOf(event to 2, event2 to 1)

    private val principal = UsernamePasswordAuthenticationToken(UserAuthenticationProvider.PersonDetails("hello@gmail.com"),
            "password", null)

    private val naturalPerson = NaturalPerson("1", "hello@gmail.com")

    private val personDto = PersonDto("hello@gmail.com")

    private val naturalPersonDto = NaturalPersonDto("hello@gmail.com")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = eventPersonController.createMockMvc()

        every { naturalPersonTranslator.toDto(naturalPerson) } returns naturalPersonDto
        every { personTranslator.toDto(naturalPerson) } returns personDto
        every { eventTranslator.toDto(event) } returns eventDto
        every { eventTranslator.toDto(event2) } returns eventDto2
    }

    @Test
    fun testFindAllEvents() {
        every { eventPersonService.findAllEvents("hello@gmail.com") } returns eventsDto

        val result = mockMvc.perform(get("/event/all-created")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(eventsDto.translateMap())
    }

    @Test
    fun testFindAllSubscribedEvents() {
        every { eventPersonService.findAllSubscribedEvents("hello@gmail.com") } returns eventsDto

        val result = mockMvc.perform(get("/event/subscribed")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(eventsDto.translateMap())
    }

    @Test
    fun testFindAllCreatedEvents() {
        every { eventPersonService.findAllCreatedEvents("hello@gmail.com") } returns eventsDto

        val result = mockMvc.perform(get("/event/created")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(eventsDto.translateMap())
    }

    @Test
    fun testSubscribe() {
        every { eventPersonService.subscribe("1", principal.name) } returns naturalPerson

        val result = mockMvc.perform(post("/event/1/subscribe")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto)
    }

    @Test
    fun testUnsubscribe() {
        every { eventPersonService.unsubscribe("1", principal.name) } returns naturalPerson

        val result = mockMvc.perform(post("/event/1/unsubscribe")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto)
    }

    @Test
    fun testGetOwner() {
        every { eventPersonService.getOwner("1") } returns naturalPerson

        val result = mockMvc.perform(get("/event/1/owner")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(personDto)
    }
}
