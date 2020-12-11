package com.example.venka.eventio.controller.person

import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.dto.person.LegalPersonDto
import com.example.venka.eventio.data.dto.person.NaturalPersonDto
import com.example.venka.eventio.data.dto.person.PersonDto
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.person.impl.PersonServiceImpl
import com.example.venka.eventio.support.ERROR_NOT_FOUND
import com.example.venka.eventio.support.ERROR_NOT_UNIQUE
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.person.PersonTranslator
import com.example.venka.eventio.utils.format.toJson
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.hasSize
import org.json.simple.JSONObject
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
import javax.xml.bind.DatatypeConverter

class PersonControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var personController: PersonController

    @MockK
    private lateinit var personService: PersonServiceImpl

    @MockK
    private lateinit var personTranslator: PersonTranslator

    @MockK
    private lateinit var eventTranslator: EventTranslator

    private val event = Event("1", "hello", location = Room())
    private val event2 = Event("2", "hello 2", location = Room())

    private val eventDto = EventDto("1", "hello", location = RoomDto())
    private val eventDto2 = EventDto("2", "hello 2", location = RoomDto())

    private val person = NaturalPerson("1", "hello@gmail.com", createdEvents = mutableSetOf(event),
            subscribedEvents = mutableSetOf(event2))

    private val person2 = LegalPerson("2", "hello2@gmail.com", createdEvents = mutableSetOf(event2))

    private val personDto = PersonDto("hello@gmail.com")

    private val personDto2 = PersonDto("hello2@gmail.com")

    private val naturalPersonDto = NaturalPersonDto("hello@gmail.com", createdEvents = mutableSetOf(eventDto),
            subscribedEvents = mutableSetOf(eventDto2))

    private val legalPersonDto = LegalPersonDto("hello2@gmail.com", createdEvents = mutableSetOf(eventDto2))

    private val principal = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello@gmail.com", location = "Purple avenue, 17"),
            null, null
    )

    private val principal2 = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello2@gmail.com", location = "Purple avenue, 17"),
            null, null
    )

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = personController.createMockMvc()

        every { personTranslator.fromDto(any()) } returns person
        every { personTranslator.fromDto(any(), person.id) } returns person
        every { personTranslator.fromDto(any(), person2.id) } returns person2

        every { personTranslator.toDto(person) } returns personDto
        every { personTranslator.toDto(person2) } returns personDto2

        every { personTranslator.toDto(person) } returns personDto
        every { personTranslator.toDto(person2) } returns personDto2
    }

    @Test
    fun testGetAll() {
        every { personService.getAll() } returns listOf(person, person2)

        val result = mockMvc.perform(get("/person").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(personDto, personDto2))
    }

    @Test
    fun testAdd() {
        every { personService.save(any()) } returns person
        every { personService.getByParam(person.email) } returns null

        val result = mockMvc.perform(post("/person").principal(principal)
                .content(personDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(personDto)
    }

    @Test
    fun testAdd_NotUnique() {
        every { personService.getByParam(person.email) } returns person

        val result = mockMvc.perform(post("/person").principal(principal)
                .content(personDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(personDto)
    }

    @Test
    fun testSave() {
        every { personService.save(person) } returns person
        every { personService.getByParam(person.email) } returns person

        val result = mockMvc.perform(put("/person").principal(principal)
                .content(personDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(personDto)
    }

    @Test
    fun testSave_NotFound() {
        every { personService.getByParam(person.email) } returns null

        val result = mockMvc.perform(put("/person").principal(principal)
                .content(personDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testSave_NotUnique() {
        every { personService.getByParam(person.email) } returns person2

        val result = mockMvc.perform(put("/person").principal(principal)
                .content(personDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_UNIQUE)
    }

    @Test
    fun testRemove() {
        every { personService.getByParam(person.email) } returns person
        every { personService.deleteById(person.id!!) } answers { }

        val result = mockMvc.perform(delete("/person/").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(personDto)
    }

    @Test
    fun testRemove_NotFound() {
        every { personService.getByParam(person.email) } returns null

        val result = mockMvc.perform(delete("/person/").principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetById() {
        every { personService.getById(person.id!!) } returns person

        val result = mockMvc.perform(get("/person/${person.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(personDto)
    }

    @Test
    fun testGetById_NotFound() {
        every { personService.getById(person2.id!!) } returns null

        val result = mockMvc.perform(get("/person/${person2.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetByParam() {
        every { personService.getByParam(person.email) } returns person

        val result = mockMvc.perform(get("/person/")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(personDto)
    }

    @Test
    fun testGetByParam_NotFound() {
        every { personService.getByParam(person.email) } returns null

        val result = mockMvc.perform(get("/person/")
                .principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetLabel() {
        every { personService.getByParam(person.email) } returns person
        every { eventTranslator.toDto(event) } returns eventDto
        every { eventTranslator.toDto(event2) } returns eventDto2

        val events = (naturalPersonDto.createdEvents +
                naturalPersonDto.subscribedEvents).toMutableSet()

        val result = mockMvc.perform(get("/person/label")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(PersonController.PersonStub(person.email, false, events))
    }

    @Test
    fun testGetLabel_False() {
        every { personService.getByParam(person2.email) } returns person2
        every { eventTranslator.toDto(event2) } returns eventDto2

        val events = legalPersonDto.createdEvents

        val result = mockMvc.perform(get("/person/label")
                .principal(principal2))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(PersonController.PersonStub(person2.email, true, events))
    }

    @Test
    fun testGetLabel_NotFound() {
        every { personService.getByParam(person.email) } returns null

        val result = mockMvc.perform(get("/person/label")
                .principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testPostToggle() {
        every { personService.toggleLabel(principal2) } answers { }
        every { personService.getByParam(person2.email) } returns person
        every { eventTranslator.toDto(event) } returns eventDto
        every { eventTranslator.toDto(event2) } returns eventDto2

        val events = (naturalPersonDto.createdEvents +
                naturalPersonDto.subscribedEvents).toMutableSet()

        val result = mockMvc.perform(post("/person/label")
                .param("isCompany", "false")
                .principal(principal2))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(PersonController.PersonStub(person2.email, false, events))
    }

    @Test
    fun testPostToggleWithPay() {
        val json = JSONObject(mapOf("sender_first_name" to person2.email))
        val encodedString = DatatypeConverter.printBase64Binary(json.toJSONString().toByteArray())

        every { personService.toggleLabel(any<UsernamePasswordAuthenticationToken>()) } answers { }
        every { personService.getByParam(person2.email) } returns person2
        every { eventTranslator.toDto(event2) } returns eventDto2

        val events = legalPersonDto.createdEvents

        val result = mockMvc.perform(post("/person/liqpay")
                .param("data", encodedString)
                .param("signature", "signature"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(3)))
                .andReturn()

        result.assertEquals(PersonController.PersonStub(person2.email, true, events))
    }
}
