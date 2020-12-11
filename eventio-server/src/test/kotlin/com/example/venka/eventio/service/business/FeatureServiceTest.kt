package com.example.venka.eventio.service.business

import com.example.venka.eventio.data.db.business.FeatureRepository
import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.service.business.impl.FeatureServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.util.Optional

class FeatureServiceTest {

    @InjectMockKs
    private lateinit var featureService: FeatureServiceImpl

    @MockK
    private lateinit var featureRepository: FeatureRepository

    private val feature: Feature = Feature("1", "hello")
    private val feature2: Feature = Feature("2", "hello 2")

    private val features = listOf(feature, feature2)

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetAll() {
        every { featureRepository.findAll(2) } returns features

        assertEquals(featureService.getAll(), features)
    }

    @Test
    fun testSave() {
        every { featureRepository.save(feature, 2) } returns feature

        featureService.save(feature)
    }

    @Test
    fun testGetById() {
        every { featureRepository.findById(feature.id, 2) } returns Optional.of(feature)
        every { featureRepository.findById(feature2.id, 2) } returns Optional.of(feature2)

        assertEquals(featureService.getById(feature.id!!), feature)
        assertEquals(featureService.getById(feature2.id!!), feature2)
    }

    @Test
    fun testDeleteById() {
        every { featureRepository.deleteById(feature2.id!!) } answers { }

        featureService.deleteById(feature2.id!!)
    }

    @Test(expectedExceptions = [UnsupportedOperationException::class])
    fun testGetByParam() {
        featureService.getByParam(feature.name)
    }

    @Test
    fun testDeleteAll() {
        every { featureRepository.deleteAll() } answers { }

        featureService.deleteAll()
    }
}

