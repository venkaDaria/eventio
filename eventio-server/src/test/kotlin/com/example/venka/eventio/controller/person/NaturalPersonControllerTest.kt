package com.example.venka.eventio.controller.person

import com.example.venka.eventio.config.security.PersonAuthority.Companion.natural
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.person.NaturalPersonDto
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.service.person.impl.NaturalPersonServiceImpl
import com.example.venka.eventio.support.ERROR_NOT_FOUND
import com.example.venka.eventio.support.ERROR_NOT_UNIQUE
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.person.NaturalPersonTranslator
import com.example.venka.eventio.utils.format.toJson
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
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

class NaturalPersonControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var naturalPersonController: NaturalPersonController

    @MockK
    private lateinit var naturalPersonService: NaturalPersonServiceImpl

    @MockK
    private lateinit var naturalPersonTranslator: NaturalPersonTranslator

    private val email = "hello@gmail.com"

    private val naturalPerson: NaturalPerson = NaturalPerson("1", email, "123")
    private val naturalPerson2: NaturalPerson = NaturalPerson("2", "hello2@gmail.com", "456")

    private val naturalPersonDto = NaturalPersonDto(email, "123")
    private val naturalPersonDto2 = NaturalPersonDto("hello2@gmail.com", "456")

    private val naturalPersons = listOf(naturalPerson, naturalPerson2)
    private val naturalPersonDtos = listOf(naturalPersonDto, naturalPersonDto2)

    private val principal = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails(email, location = "Purple avenue, 17"),
            null, natural
    )

    private val principal2 = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello2@gmail.com", location = "Purple avenue, 17"),
            null, natural
    )

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = naturalPersonController.createMockMvc()

        every { naturalPersonTranslator.fromDto(naturalPersonDto) } returns naturalPerson
        every { naturalPersonTranslator.fromDto(naturalPersonDto, naturalPerson.id) } returns naturalPerson
        every { naturalPersonTranslator.fromDto(naturalPersonDto2, naturalPerson2.id) } returns naturalPerson2

        every { naturalPersonTranslator.toDto(naturalPerson) } returns naturalPersonDto
        every { naturalPersonTranslator.toDto(naturalPerson2) } returns naturalPersonDto2
    }

    @Test
    fun testGetAll() {
        every { naturalPersonService.getAll() } returns naturalPersons

        val result = mockMvc.perform(get("/natural-person").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(naturalPersonDtos)
    }

    @Test
    fun testAdd() {
        every { naturalPersonService.save(any()) } returns naturalPerson
        every { naturalPersonService.getByParam(naturalPerson.email) } returns null

        val result = mockMvc.perform(post("/natural-person")
                .content(naturalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto)
    }

    @Test
    fun testAdd_NotUnique() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns naturalPerson

        val result = mockMvc.perform(post("/natural-person")
                .content(naturalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_UNIQUE)
    }

    @Test
    fun testSave() {
        every { naturalPersonService.save(naturalPerson) } returns naturalPerson
        every { naturalPersonService.getByParam(naturalPerson.email) } returns naturalPerson

        val result = mockMvc.perform(put("/natural-person").principal(principal)
                .content(naturalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto)
    }

    @Test
    fun testSave_NotFound() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns null

        val result = mockMvc.perform(put("/natural-person").principal(principal)
                .content(naturalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testSave_NotUnique() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns naturalPerson2

        val result = mockMvc.perform(put("/natural-person").principal(principal)
                .content(naturalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_UNIQUE)
    }

    @Test
    fun testSave_PlacesEmpty() {
        every { naturalPersonService.save(naturalPerson2) } returns naturalPerson2
        every { naturalPersonService.getByParam(naturalPerson2.email) } returns naturalPerson2

        val result = mockMvc.perform(put("/natural-person").principal(principal2)
                .content(naturalPersonDto2.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto2)
    }

    @Test
    fun testSave_ChangeEmail() {
        every { naturalPersonService.save(naturalPerson) } returns naturalPerson
        every { naturalPersonService.getByParam(naturalPerson.email) } returns naturalPerson
        every { naturalPersonService.getByParam("hello") } returns null

        naturalPersonDto.email = "hello"
        naturalPerson.email = "hello"

        val result = mockMvc.perform(put("/natural-person").principal(principal)
                .content(naturalPersonDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto)

        naturalPersonDto.email = email
        naturalPerson.email = email
    }

    @Test
    fun testRemove() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns naturalPerson
        every { naturalPersonService.deleteById(naturalPerson.id!!) } answers { }

        val result = mockMvc.perform(delete("/natural-person/").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto)
    }

    @Test
    fun testRemove_NotFound() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns null

        val result = mockMvc.perform(delete("/natural-person/").principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetById() {
        every { naturalPersonService.getById(naturalPerson.id!!) } returns naturalPerson

        val result = mockMvc.perform(get("/natural-person/${naturalPerson.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto)
    }

    @Test
    fun testGetById_NotFound() {
        every { naturalPersonService.getById(naturalPerson2.id!!) } returns null

        val result = mockMvc.perform(get("/natural-person/${naturalPerson2.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetByParam() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns naturalPerson

        val result = mockMvc.perform(get("/natural-person/")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(5)))
                .andReturn()

        result.assertEquals(naturalPersonDto)
    }

    @Test
    fun testGetByParam_NotFound() {
        every { naturalPersonService.getByParam(naturalPerson.email) } returns null

        val result = mockMvc.perform(get("/natural-person/")
                .principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }
}
