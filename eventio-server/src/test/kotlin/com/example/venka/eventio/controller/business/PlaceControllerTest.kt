package com.example.venka.eventio.controller.business

import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.MessageStub
import com.example.venka.eventio.data.model.MessageType
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.business.PlaceService
import com.example.venka.eventio.service.business.RoomService
import com.example.venka.eventio.service.mail.MailService
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.support.ERROR_NOT_FOUND
import com.example.venka.eventio.support.ERROR_NOT_UNIQUE
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.business.PlaceTranslator
import com.example.venka.eventio.utils.format.formatToString
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
import java.time.LocalDateTime

class PlaceControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var placeController: PlaceController

    @MockK
    private lateinit var placeService: PlaceService

    @MockK
    private lateinit var roomService: RoomService

    @MockK
    private lateinit var legalPersonService: LegalPersonService

    @MockK
    private lateinit var placeTranslator: PlaceTranslator

    @MockK
    private lateinit var eventService: EventService

    @MockK
    private lateinit var mailService: MailService

    private val room = Room("1", "hello")
    private val room2 = Room("2", "hello2")

    private val roomDto = RoomDto("1", "hello")
    private val roomDto2 = RoomDto("2", "hello2")

    private val place = Place("1", "Cool place", "Purple avenue, 17",
            rooms = mutableSetOf(room))
    private val place2 = Place("2", "Cool place 2", "Strange address")
    private val place3 = Place("1", "Cool place", "Purple avenue, 17",
            rooms = mutableSetOf(room2))

    private val placeDto = PlaceDto("1", "Cool place", "Purple avenue, 17",
            rooms = mutableSetOf(roomDto))
    private val placeDto2 = PlaceDto("2", "Cool place 2", "Strange address")
    private val placeDto3 = PlaceDto("1", "Cool place", "Purple avenue, 17",
            rooms = mutableSetOf(roomDto2))

    private val legalPerson: LegalPerson = LegalPerson("1", "hello@gmail.com", "123")
    private val principal = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello@gmail.com", location = "Purple avenue, 17"),
            null, null
    )

    private val localDateTime = LocalDateTime.now().formatToString()
    private val localDateTime2 = LocalDateTime.now().plusDays(1).formatToString()

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = placeController.createMockMvc()

        every { placeTranslator.fromDto(placeDto) } returns place
        every { placeTranslator.fromDto(placeDto, place.id) } returns place
        every { placeTranslator.fromDto(placeDto3, place.id) } returns place3

        every { placeTranslator.toDto(place) } returns placeDto
        every { placeTranslator.toDto(place2) } returns placeDto2
    }

    @Test
    fun testGetAll() {
        every { placeService.getAll() } returns listOf(place, place2)

        val result = mockMvc.perform(get("/place").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(placeDto, placeDto2))
    }

    @Test
    fun testGetAll_LocationFilter() {
        every { placeService.getAll() } returns listOf(place, place2)

        val result = mockMvc.perform(get("/place")
                .param("locationFilter", "true")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(1)))
                .andReturn()

        result.assertEquals(listOf(placeDto))
    }

    @Test
    fun testGetAll_LocationFilter_False() {
        every { placeService.getAll() } returns listOf(place, place2)

        val result = mockMvc.perform(get("/place")
                .param("locationFilter", "false")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(placeDto, placeDto2))
    }

    @Test
    fun testGetAll_LocationFilter_Null() {
        val principal = UsernamePasswordAuthenticationToken(
                UserAuthenticationProvider.PersonDetails("hello@gmail.com"),
                null, null
        )

        every { placeService.getAll() } returns listOf(place, place2)

        val result = mockMvc.perform(get("/place")
                .param("locationFilter", "true")
                .principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(placeDto, placeDto2))
    }

    @Test
    fun testAdd() {
        every { placeService.save(any()) } returns place
        every { legalPersonService.getByParam(principal.name) } returns legalPerson
        every { placeService.getByParam(place.realAddress) } returns null
        every { legalPersonService.save(legalPerson) } returns legalPerson

        val result = mockMvc.perform(post("/place").principal(principal)
                .content(placeDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testAdd_NotFound() {
        every { placeService.save(any()) } returns place
        every { legalPersonService.getByParam(principal.name) } returns null
        every { placeService.getByParam(place.realAddress) } returns null

        val result = mockMvc.perform(post("/place").principal(principal)
                .content(placeDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testAdd_NotUnique() {
        every { placeService.getByParam(place.realAddress) } returns place

        val result = mockMvc.perform(post("/place").principal(principal)
                .content(placeDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_UNIQUE)
    }

    @Test
    fun testSave() {
        every { placeService.save(place) } returns place
        every { placeService.getByParam(place.realAddress) } returns place
        every { eventService.setInvalidLabelByRoomIds(emptyList()) } returns listOf()

        val result = mockMvc.perform(put("/place").principal(principal)
                .content(placeDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testSave_DiffExists() {
        every { placeService.save(place) } returns place
        every { placeService.getByParam(place.realAddress) } returns place3
        every { eventService.setInvalidLabelByRoomIds(listOf(room2.id!!)) } returns listOf()
        every { roomService.deleteById(room2.id!!) } answers { }

        val result = mockMvc.perform(put("/place").principal(principal)
                .content(placeDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testSave_RoomsEmpty() {
        val rooms = place.rooms
        place.rooms = mutableSetOf()

        every { placeService.save(place) } returns place
        every { placeService.getByParam(place.realAddress) } returns place
        every { eventService.setInvalidLabelByRoomIds(emptyList()) } returns listOf()

        val result = mockMvc.perform(put("/place").principal(principal)
                .content(placeDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)

        place.rooms = rooms
    }

    @Test
    fun testSave_NotFound() {
        every { placeService.getByParam(place.realAddress) } returns null

        val result = mockMvc.perform(put("/place").principal(principal)
                .content(placeDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testSave_EventsNotEmpty() {
        objectMockk(MessageType.INVALID_EVENT).use {
            every { MessageType.INVALID_EVENT.simpleMessage } returns MessageStub(templateName="hello")

            every { placeService.save(place) } returns place
            every { placeService.getByParam(place.realAddress) } returns place
            every { eventService.setInvalidLabelByRoomIds(emptyList()) } returns listOf("1")
            every {
                mailService.sendMessage(legalPerson.email, MessageStub(templateName="hello"), listOf("1"))
            } answers { }

            val result = mockMvc.perform(put("/place").principal(principal)
                    .content(placeDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().`is`(200))
                    .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                    .andReturn()

            result.assertEquals(placeDto)
        }
    }

    @Test
    fun testRemove() {
        every { placeService.getById(place.id!!) } returns place
        every { placeService.deleteById(place.id!!) } answers { }
        every { eventService.setInvalidLabelByPlace(place.id!!) } returns listOf()

        val result = mockMvc.perform(delete("/place/${place.id}").principal(principal))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testRemove_EventsNotEmpty() {
        objectMockk(MessageType.INVALID_EVENT).use {
            every { MessageType.INVALID_EVENT.simpleMessage } returns MessageStub(templateName="hello")

            every { placeService.getById(place.id!!) } returns place
            every { placeService.deleteById(place.id!!) } answers { }
            every { eventService.setInvalidLabelByPlace(place.id!!) } returns listOf("1")
            every {
                mailService.sendMessage(legalPerson.email, MessageStub(templateName="hello"), listOf("1"))
            } answers { }

            val result = mockMvc.perform(delete("/place/${place.id}").principal(principal))
                    .andExpect(status().`is`(200))
                    .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                    .andReturn()

            result.assertEquals(placeDto)
        }
    }

    @Test
    fun testRemove_NotFound() {
        every { placeService.getById(place.id!!) } returns null

        val result = mockMvc.perform(delete("/place/${place.id}").principal(principal))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetById() {
        every { placeService.getById(place.id!!) } returns place

        val result = mockMvc.perform(get("/place/${place.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testGetById_WithStart() {
        every { placeService.getById(place.id!!) } returns place
        every { eventService.isFree("1", localDateTime, null) } returns true

        val result = mockMvc.perform(get("/place/${place.id}?start=$localDateTime"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testGetById_WithStart_End() {
        every { placeService.getById(place.id!!) } returns place
        every { eventService.isFree("1", localDateTime, localDateTime2) } returns true

        val result = mockMvc.perform(get(
                "/place/${place.id}?start=$localDateTime&end=$localDateTime2"
                )).andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testGetById_NotFound() {
        every { placeService.getById(place2.id!!) } returns null

        val result = mockMvc.perform(get("/place/${place2.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetByParam() {
        every { placeService.getByParam(place.realAddress) } returns place

        val result = mockMvc.perform(get("/place/")
                .param("param", place.realAddress))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testGetByParam_NotFound() {
        every { placeService.getByParam(place.realAddress) } returns null

        val result = mockMvc.perform(get("/place/")
                .param("param", place.realAddress))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetByRoomId() {
        every { placeService.getByRoomId(room.id!!) } returns place

        val result = mockMvc.perform(get("/place/room/${room.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(7)))
                .andReturn()

        result.assertEquals(placeDto)
    }

    @Test
    fun testGetByRoomId_NotFound() {
        every { placeService.getByRoomId(room.id!!) } returns null

        val result = mockMvc.perform(get("/place/room/${room.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }
}
