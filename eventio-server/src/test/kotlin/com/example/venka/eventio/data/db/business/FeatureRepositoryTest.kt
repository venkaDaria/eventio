package com.example.venka.eventio.data.db.business

import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.service.business.impl.FeatureServiceImpl
import com.example.venka.eventio.support.db.business.BootstrapFeature
import com.example.venka.eventio.support.db.business.feature
import com.example.venka.eventio.support.db.business.feature2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@DataNeo4jTest
class FeatureRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var featureRepository: FeatureRepository

    private lateinit var featureService: FeatureServiceImpl

    private val feature3: Feature = Feature("3", "hello 3")

    private val features = listOf(feature, feature2)

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapFeature(featureRepository)
        bootstrapPerson.run()

        featureService = FeatureServiceImpl(featureRepository)
    }

    @Test
    fun testGetAll() {
        assertEquals(featureService.getAll(), features)
    }

    @Test
    fun testSave() {
        featureService.save(feature3)

        assertEquals(featureService.getAll(), listOf(feature, feature2, feature3))
    }

    @Test
    fun testGetById() {
        assertEquals(featureService.getById(feature.id!!), feature)
        assertEquals(featureService.getById(feature2.id!!), feature2)
    }

    @Test
    fun testDeleteById() {
        featureService.deleteById(feature2.id!!)

        assertEquals(featureService.getAll(), listOf(feature))
    }

    @Test(expectedExceptions = [UnsupportedOperationException::class])
    fun testGetByParam() {
        featureService.getByParam(feature.name)
    }

    @Test
    fun testDeleteAll() {
        featureService.deleteAll()

        assertEquals(featureService.getAll(), emptyList<Feature>())
    }
}

