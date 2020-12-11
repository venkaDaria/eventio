package com.example.venka.eventio.controller

import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.MessageStub
import com.example.venka.eventio.data.model.MessageType
import com.example.venka.eventio.data.model.Mode
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.business.PlaceService
import com.example.venka.eventio.service.complex.impl.EventPersonService
import com.example.venka.eventio.service.mail.MailService
import com.example.venka.eventio.support.ERROR_NOT_FOUND
import com.example.venka.eventio.support.ERROR_NOT_UNIQUE
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.utils.format.toJson
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.objectMockk
import io.mockk.use
import org.hamcrest.Matchers.hasSize
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class EventControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var eventController: EventController

    @MockK
    private lateinit var eventService: EventService

    @MockK
    private lateinit var placeService: PlaceService

    @MockK
    private lateinit var eventTranslator: EventTranslator

    @MockK
    private lateinit var mailService: MailService

    @MockK
    private lateinit var eventPersonService: EventPersonService

    private val event = Event("1", "hello", location = Room("1", "hello3"),
            mode = Mode.PUBLIC)
    private val event2 = Event("2", "hello 2", location = Room("2", "hello"), mode = Mode.PRIVATE)

    private val eventDto = EventDto("1", "hello", location = RoomDto("1", "hello3"),
            mode = Mode.PUBLIC)
    private val eventDto2 = EventDto("2", "hello 2", location = RoomDto("2", "hello"),
            mode = Mode.PRIVATE)

    private val place = Place(realAddress = "Центральный парк культуры и отдыха им. М. Горького")

    private val principal = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello@gmail.com", place.realAddress),
            null, null
    )

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = eventController.createMockMvc()

        every { eventTranslator.fromDto(eventDto) } returns event
        every { eventTranslator.fromDto(eventDto, event.id) } returns event

        every { eventTranslator.toDto(event2) } returns eventDto2
        every { eventTranslator.toDto(event) } returns eventDto

        place.rooms.add(event2.location)
    }

    @Test
    fun testGetAll() {
        every { eventService.getAll() } returns listOf(event, event2)

        val result = mockMvc.perform(get("/event").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(eventDto, eventDto2))
    }

    @Test
    fun testGetAll_IsPublic() {
        every { eventService.getAll() } returns listOf(event, event2)

        val result = mockMvc.perform(get("/event")
                .param("isPublic", "true")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(1)))
                .andReturn()

        result.assertEquals(listOf(eventDto))
    }

    @Test
    fun testGetAll_IsPublic_False() {
        every { eventService.getAll() } returns listOf(event, event2)

        val result = mockMvc.perform(get("/event")
                .param("isPublic", "false")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(eventDto, eventDto2))
    }

    @Test
    fun testGetAll_LocationFilter() {
        every { eventService.getAll() } returns listOf(event, event2)
        every { placeService.getAll() } returns listOf(place)

        val result = mockMvc.perform(get("/event")
                .param("locationFilter", "true")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(1)))
                .andReturn()

        result.assertEquals(listOf(eventDto2))
    }

    @Test
    fun testGetAll_LocationFilter_False() {
        every { eventService.getAll() } returns listOf(event, event2)
        every { placeService.getAll() } returns listOf(place)

        val result = mockMvc.perform(get("/event")
                .param("locationFilter", "false")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(eventDto, eventDto2))
    }

    @Test
    fun testGetAll_LocationFilter_Null() {
        val principal = UsernamePasswordAuthenticationToken(
                UserAuthenticationProvider.PersonDetails("hello@email.com"),
                null, null
        )

        every { eventService.getAll() } returns listOf(event, event2)

        val result = mockMvc.perform(get("/event")
                .param("locationFilter", "true")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(eventDto, eventDto2))
    }

    @Test
    fun testAdd() {
        objectMockk(MessageType.NEW_EVENT_FOR_CREATOR).use {
            every { MessageType.NEW_EVENT_FOR_CREATOR.simpleMessage } returns MessageStub(templateName="hello")

            objectMockk(MessageType.NEW_EVENT_FOR_COMPANY).use {
                every { MessageType.NEW_EVENT_FOR_COMPANY.simpleMessage } returns MessageStub(templateName="hello")

                every { eventService.save(any()) } returns event
                every { eventService.getByParam(event.title) } returns null
                every { placeService.getOwner(event.location.id!!) } returns LegalPerson(email="hello2@gmail.com")

                every { eventPersonService.create(event, principal.name) } returns null

                every { mailService.sendMessage("hello@gmail.com", MessageStub(templateName="hello"), event) } answers { }
                every { mailService.sendMessage("hello2@gmail.com", MessageStub(templateName="hello"), event) } answers { }

                val result = mockMvc.perform(post("/event").principal(principal)
                        .content(eventDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().`is`(200))
                        .andExpect(jsonPath("$.*", hasSize<Collection<*>>(8)))
                        .andReturn()

                result.assertEquals(eventDto)
            }
        }
    }

    @Test
    fun testAdd_NotUnique() {
        every { eventService.getByParam(event.title) } returns event

        val result = mockMvc.perform(post("/event").principal(principal)
                .content(eventDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_UNIQUE)
    }

    @Test
    fun testSave() {
        objectMockk(MessageType.UPDATED_EVENT_FOR_SUBSCRIBERS).use {
            every { MessageType.UPDATED_EVENT_FOR_SUBSCRIBERS.simpleMessage } returns MessageStub(templateName="hello")


            objectMockk(MessageType.UPDATED_EVENT_FOR_COMPANY).use {
                every { MessageType.UPDATED_EVENT_FOR_COMPANY.simpleMessage } returns MessageStub(templateName="hello")

                every { eventService.save(event) } returns event
                every { eventService.getByParam(event.title) } returns event
                every { eventService.getSubscribers(event.id!!) } returns listOf("hello@gmail.com")
                every { placeService.getOwner(event.location.id!!) } returns LegalPerson(email="hello2@gmail.com")

                every { mailService.sendMessage("hello@gmail.com", MessageStub(templateName="hello"), event) } answers { }
                every { mailService.sendMessage("hello2@gmail.com", MessageStub(templateName="hello"), event) } answers { }

                val result = mockMvc.perform(put("/event")
                        .content(eventDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().`is`(200))
                        .andExpect(jsonPath("$.*", hasSize<Collection<*>>(8)))
                        .andReturn()

                result.assertEquals(eventDto)
            }
        }
    }

    @Test
    fun testSave_NotFound() {
        every { eventService.getByParam(event.title) } returns null

        val result = mockMvc.perform(put("/event")
                .content(eventDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testRemove() {
        every { eventService.getById(event.id!!) } returns event
        every { eventService.deleteById(event.id!!) } answers { }

        val result = mockMvc.perform(delete("/event/${event.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(8)))
                .andReturn()

        result.assertEquals(eventDto)
    }

    @Test
    fun testRemove_NotFound() {
        every { eventService.getById(event.id!!) } returns null

        val result = mockMvc.perform(delete("/event/${event.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetById() {
        every { eventService.getById(event.id!!) } returns event
        every { eventService.getOwner(event.id!!) } returns NaturalPerson(email = principal.name)

        val result = mockMvc.perform(get("/event/${event.id}").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(8)))
                .andReturn()

        result.assertEquals(eventDto)
    }

    @Test
    fun testGetById_Private() {
        every { eventService.getById(event2.id!!) } returns event2
        every { eventService.getOwner(event2.id!!) } returns NaturalPerson(email = principal.name)

        val result = mockMvc.perform(get("/event/${event2.id}").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(8)))
                .andReturn()

        result.assertEquals(eventDto2)
    }

    @Test
    fun testGetById_Private_NotMine() {
        every { eventService.getById(event2.id!!) } returns event2
        every { eventService.getOwner(event2.id!!) } returns NaturalPerson(email = "another person")

        val result = mockMvc.perform(get("/event/${event2.id}").principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetById_NotFound() {
        every { eventService.getById(event2.id!!) } returns null

        val result = mockMvc.perform(get("/event/${event2.id}").principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetByParam() {
        every { eventService.getByParam(event.title) } returns event

        val result = mockMvc.perform(get("/event/")
                .param("param", event.title))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(8)))
                .andReturn()

        result.assertEquals(eventDto)
    }

    @Test
    fun testGetByParam_NotFound() {
        every { eventService.getByParam(event2.title) } returns null

        val result = mockMvc.perform(get("/event/")
                .param("param", event2.title))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testDeleteAllInvalid() {
        every { eventService.deleteAllInvalid() } returns listOf(event.id!!)

        val result = mockMvc.perform(delete("/event/invalid"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(1)))
                .andReturn()

        result.assertEquals(listOf(eventDto.id))
    }
}
