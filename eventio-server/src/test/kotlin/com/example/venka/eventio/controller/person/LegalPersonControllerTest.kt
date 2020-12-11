package com.example.venka.eventio.controller.person

import com.example.venka.eventio.config.security.PersonAuthority.Companion.legal
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.dto.person.LegalPersonDto
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.MessageStub
import com.example.venka.eventio.data.model.MessageType
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.mail.MailService
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import com.example.venka.eventio.support.ERROR_NOT_FOUND
import com.example.venka.eventio.support.ERROR_NOT_UNIQUE
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.person.LegalPersonTranslator
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
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class LegalPersonControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var legalPersonController: LegalPersonController

    @MockK
    private lateinit var legalPersonService: LegalPersonServiceImpl

    @MockK
    private lateinit var legalPersonTranslator: LegalPersonTranslator

    @MockK
    private lateinit var eventService: EventService

    @MockK
    private lateinit var mailService: MailService

    private val mutableSet = mutableSetOf(Place(id = "1", realAddress = "Purple avenue, 17"),
            Place("2", "real address"))

    private val mutableSetDto = mutableSetOf(PlaceDto(id = "1", realAddress = "Purple avenue, 17"),
            PlaceDto("2", "real address"))

    private val email = "hello@gmail.com"

    private val legalPerson = LegalPerson("1", email, "123", places = mutableSet)

    private val legalPerson2 = LegalPerson("2", "hello2@gmail.com", "456")

    private val legalPersonDto = LegalPersonDto(email, "123", places = mutableSetDto)

    private val legalPersonDto2 = LegalPersonDto("hello2@gmail.com", "456")

    private val legalPersons = listOf(legalPerson, legalPerson2)
    private val legalPersonDtos = listOf(legalPersonDto, legalPersonDto2)

    private val principal = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails(email, location = "Purple avenue, 17"),
            null, legal
    )

    private val principal2 = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello2@gmail.com", location = "Purple avenue, 17"),
            null, legal
    )

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = legalPersonController.createMockMvc()

        every { legalPersonTranslator.fromDto(legalPersonDto) } returns legalPerson
        every { legalPersonTranslator.fromDto(legalPersonDto, legalPerson.id) } returns legalPerson
        every { legalPersonTranslator.fromDto(legalPersonDto2, legalPerson2.id) } returns legalPerson2

        every { legalPersonTranslator.toDto(legalPerson) } returns legalPersonDto
        every { legalPersonTranslator.toDto(legalPerson2) } returns legalPersonDto2
    }

    @Test
    fun testGetAll() {
        every { legalPersonService.getAll() } returns legalPersons

        val result = mockMvc.perform(get("/legal-person").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(legalPersonDtos)
    }

    @Test
    fun testGetAll_LocationFilter() {
        every { legalPersonService.getAll() } returns listOf(legalPerson, legalPerson2)

        val result = mockMvc.perform(get("/legal-person")
                .param("locationFilter", "true")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(1)))
                .andReturn()

        result.assertEquals(listOf(legalPersonDto))
    }

    @Test
    fun testGetAll_LocationFilter_False() {
        every { legalPersonService.getAll() } returns listOf(legalPerson, legalPerson2)

        val result = mockMvc.perform(get("/legal-person")
                .param("locationFilter", "false")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(legalPersonDto, legalPersonDto2))
    }

    @Test
    fun testGetAll_LocationFilter_Null() {
        val principal = UsernamePasswordAuthenticationToken(
                UserAuthenticationProvider.PersonDetails(email),
                null, null
        )

        every { legalPersonService.getAll() } returns listOf(legalPerson, legalPerson2)

        val result = mockMvc.perform(get("/legal-person")
                .param("locationFilter", "true")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(legalPersonDtos)
    }

    @Test
    fun testGetPlaces() {
        every { legalPersonService.getByCompanyUrl("123") } returns legalPerson

        val result = mockMvc.perform(get("/legal-person/123/places"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(mutableSetDto)
    }

    @Test
    fun testGetPlaces_NotFound() {
        every { legalPersonService.getByCompanyUrl("123") } returns null

        val result = mockMvc.perform(get("/legal-person/123/places")
                .principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetPlaceById() {
        every { legalPersonService.getByParam(legalPerson.email) } returns legalPerson
        every { legalPersonTranslator.toDto(legalPerson) } returns legalPersonDto

        val result = mockMvc.perform(get("/legal-person/places/1")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(mutableSetDto.first())
    }

    @Test
    fun testGetPlacesById_NotFound() {
        every { legalPersonService.getByParam(legalPerson.email) } returns null

        val result = mockMvc.perform(get("/legal-person/places/1")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andReturn()

        assertTrue(result.response.contentAsString.isEmpty())
    }

    @Test
    fun testAdd() {
        every { legalPersonService.fixCompanyUrl(any()) } returns legalPerson
        every { legalPersonService.save(legalPerson) } returns legalPerson
        every { legalPersonService.getByParam(legalPerson.email) } returns null

        val result = mockMvc.perform(post("/legal-person")
                .content(legalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(legalPersonDto)
    }

    @Test
    fun testAdd_NotUnique() {
        every { legalPersonService.getByParam(legalPerson.email) } returns legalPerson

        val result = mockMvc.perform(post("/legal-person")
                .content(legalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_UNIQUE)
    }

    @Test
    fun testSave() {
        every { legalPersonService.fixCompanyUrl(legalPerson) } returns legalPerson
        every { legalPersonService.save(legalPerson) } returns legalPerson
        every { legalPersonService.getByParam(legalPerson.email) } returns legalPerson

        val result = mockMvc.perform(put("/legal-person").principal(principal)
                .content(legalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(legalPersonDto)
    }

    @Test
    fun testSave_NotFound() {
        every { legalPersonService.fixCompanyUrl(legalPerson) } returns legalPerson
        every { legalPersonService.getByParam(legalPerson.email) } returns null

        val result = mockMvc.perform(put("/legal-person").principal(principal)
                .content(legalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testSave_NotUnique() {
        every { legalPersonService.fixCompanyUrl(legalPerson) } returns legalPerson2
        every { legalPersonService.getByParam(legalPerson.email) } returns legalPerson2

        val result = mockMvc.perform(put("/legal-person").principal(principal)
                .content(legalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_UNIQUE)
    }

    @Test
    fun testSave_PlacesEmpty() {
        every { legalPersonService.fixCompanyUrl(legalPerson2) } returns legalPerson2
        every { legalPersonService.save(legalPerson2) } returns legalPerson2
        every { legalPersonService.getByParam(legalPerson2.email) } returns legalPerson2

        val result = mockMvc.perform(put("/legal-person").principal(principal2)
                .content(legalPersonDto2.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(legalPersonDto2)
    }

    @Test
    fun testSave_ChangeEmail() {
        every { legalPersonService.fixCompanyUrl(legalPerson) } returns legalPerson
        every { legalPersonService.save(legalPerson) } returns legalPerson
        every { legalPersonService.getByParam(legalPerson.email) } returns legalPerson
        every { legalPersonService.getByParam("hello") } returns null

        legalPersonDto.email = "hello"
        legalPerson.email = "hello"

        val result = mockMvc.perform(put("/legal-person").principal(principal)
                .content(legalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(legalPersonDto)

        legalPersonDto.email = email
        legalPerson.email = email
    }

    @Test
    fun testRemove() {
        objectMockk(MessageType.INVALID_EVENT).use {
            every { MessageType.INVALID_EVENT.simpleMessage } returns MessageStub(templateName="hello")

            every { legalPersonService.getByParam(legalPerson.email) } returns legalPerson
            every { legalPersonService.deleteById(legalPerson.id!!) } answers { }
            every { eventService.setInvalidLabelByPerson(legalPerson.email) } returns listOf()
            every { mailService.sendMessage(legalPerson.email, MessageStub(templateName="hello")) } answers { }

            val result = mockMvc.perform(delete("/legal-person/").principal(principal))
                    .andExpect(status().`is`(200))
                    .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                    .andReturn()

            result.assertEquals(legalPersonDto)
        }
    }

    @Test
    fun testRemove_NotFound() {
        every { legalPersonService.getByParam(legalPerson.email) } returns null

        val result = mockMvc.perform(delete("/legal-person/").principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetById() {
        every { legalPersonService.getById(legalPerson.id!!) } returns legalPerson

        val result = mockMvc.perform(get("/legal-person/${legalPerson.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(legalPersonDto)
    }

    @Test
    fun testGetById_NotFound() {
        every { legalPersonService.getById(legalPerson2.id!!) } returns null

        val result = mockMvc.perform(get("/legal-person/${legalPerson2.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetByParam() {
        every { legalPersonService.getByParam(legalPerson.email) } returns legalPerson

        val result = mockMvc.perform(get("/legal-person/")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(legalPersonDto)
    }

    @Test
    fun testGetByParam_NotFound() {
        every { legalPersonService.getByParam(legalPerson.email) } returns null

        val result = mockMvc.perform(get("/legal-person/")
                .principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }
}
