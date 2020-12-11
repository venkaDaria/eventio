package com.example.venka.eventio.controller.business

import com.example.venka.eventio.data.dto.business.FeatureDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.business.impl.RoomServiceImpl
import com.example.venka.eventio.support.ERROR_NOT_FOUND
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.business.FeatureTranslator
import com.example.venka.eventio.translator.business.RoomTranslator
import com.example.venka.eventio.utils.format.toJson
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.hasSize
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class RoomControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var roomController: RoomController

    @MockK
    private lateinit var roomService: RoomServiceImpl

    @MockK
    private lateinit var roomTranslator: RoomTranslator

    @MockK
    private lateinit var featureTranslator: FeatureTranslator

    private val room = Room("1", "hello")
    private val room2 = Room("2", "hello 2")

    private val roomDto = RoomDto("1", "hello")
    private val roomDto2 = RoomDto("2", "hello 2")

    private val feature = Feature("1", "hello")
    private val featureDto = FeatureDto("1", "hello")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = roomController.createMockMvc()

        every { roomTranslator.fromDto(roomDto) } returns room
        every { roomTranslator.fromDto(roomDto, room.id) } returns room

        every { roomTranslator.toDto(room) } returns roomDto
        every { roomTranslator.toDto(room2) } returns roomDto2
    }

    @Test
    fun testGetAll() {
        every { roomService.getAll() } returns listOf(room, room2)

        val result = mockMvc.perform(get("/room"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(roomDto, roomDto2))
    }

    @Test
    fun testAdd() {
        every { roomService.save(any()) } returns room

        val result = mockMvc.perform(post("/room")
                .content(roomDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(roomDto)
    }

    @Test
    fun testAddFeature() {
        every { roomService.getById(room.id!!) } returns room
        every { featureTranslator.fromDto(featureDto) } returns feature
        every { roomService.save(room) } returns room
        every { featureTranslator.toDto(feature) } returns featureDto

        val result = mockMvc.perform(put("/room/${room.id}")
                .content(featureDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(featureDto)
    }

    @Test
    fun testAddFeature_NotFound() {
        every { roomService.getById(room.id!!) } returns null

        val result = mockMvc.perform(put("/room/${room.id}")
                .content(featureDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testSave() {
        every { roomService.save(room) } returns room
        every { roomService.getById(room.id!!) } returns room

        val result = mockMvc.perform(put("/room")
                .content(roomDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(roomDto)
    }

    @Test
    fun testSave_NullId() {
        every { roomService.save(room) } returns Room()

        val result = mockMvc.perform(put("/room")
                .content(roomDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testSave_NotFound() {
        every { roomService.getById(room.id!!) } returns null

        val result = mockMvc.perform(put("/room")
                .content(roomDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testRemove() {
        every { roomService.getById(room.id!!) } returns room
        every { roomService.deleteById(room.id!!) } answers { }

        val result = mockMvc.perform(delete("/room/${room.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(roomDto)
    }

    @Test
    fun testRemove_NotFound() {
        every { roomService.getById(room.id!!) } returns null

        val result = mockMvc.perform(delete("/room/${room.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetById() {
        every { roomService.getById(room.id!!) } returns room

        val result = mockMvc.perform(get("/room/${room.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(6)))
                .andReturn()

        result.assertEquals(roomDto)
    }

    @Test
    fun testGetById_NotFound() {
        every { roomService.getById(room2.id!!) } returns null

        val result = mockMvc.perform(get("/room/${room2.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }
}
