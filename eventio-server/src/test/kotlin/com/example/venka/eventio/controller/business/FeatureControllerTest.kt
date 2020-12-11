package com.example.venka.eventio.controller.business

import com.example.venka.eventio.data.dto.business.FeatureDto
import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.service.business.impl.FeatureServiceImpl
import com.example.venka.eventio.support.ERROR_NOT_FOUND
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import com.example.venka.eventio.translator.business.FeatureTranslator
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

class FeatureControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var featureController: FeatureController

    @MockK
    private lateinit var featureService: FeatureServiceImpl

    @MockK
    private lateinit var featureTranslator: FeatureTranslator

    private val feature = Feature("1", "hello")
    private val feature2 = Feature("2", "hello 2")

    private val featureDto = FeatureDto("1", "hello")
    private val featureDto2: FeatureDto = FeatureDto("2", "hello 2")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = featureController.createMockMvc()

        every { featureTranslator.fromDto(featureDto) } returns feature
        every { featureTranslator.fromDto(featureDto, feature.id) } returns feature

        every { featureTranslator.toDto(feature) } returns featureDto
        every { featureTranslator.toDto(feature2) } returns featureDto2
    }

    @Test
    fun testGetAll() {
        every { featureService.getAll() } returns listOf(feature, feature2)

        val result = mockMvc.perform(get("/feature"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(listOf(featureDto, featureDto2))
    }

    @Test
    fun testAdd() {
        every { featureService.save(any()) } returns feature

        val result = mockMvc.perform(post("/feature")
                .content(featureDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(featureDto)
    }

    @Test
    fun testSave() {
        every { featureService.save(feature) } returns feature
        every { featureService.getById(feature.id!!) } returns feature

        val result = mockMvc.perform(put("/feature")
                .content(featureDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(featureDto)
    }

    @Test
    fun testSave_NullId() {
        every { featureService.save(feature) } returns Feature()

        val result = mockMvc.perform(put("/feature")
                .content(featureDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testSave_NotFound() {
        every { featureService.getById(feature.id!!) } returns null

        val result = mockMvc.perform(put("/feature")
                .content(featureDto.toJson()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testRemove() {
        every { featureService.getById(feature.id!!) } returns feature
        every { featureService.deleteById(feature.id!!) } answers { }

        val result = mockMvc.perform(delete("/feature/${feature.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(featureDto)
    }

    @Test
    fun testRemove_NotFound() {
        every { featureService.getById(feature.id!!) } returns null

        val result = mockMvc.perform(delete("/feature/${feature.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }

    @Test
    fun testGetById() {
        every { featureService.getById(feature.id!!) } returns feature

        val result = mockMvc.perform(get("/feature/${feature.id}"))
                .andExpect(status().`is`(200))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(featureDto)
    }

    @Test
    fun testGetById_NotFound() {
        every { featureService.getById(feature2.id!!) } returns null

        val result = mockMvc.perform(get("/feature/${feature2.id}"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andReturn()

        result.assertEquals(ERROR_NOT_FOUND)
    }
}
