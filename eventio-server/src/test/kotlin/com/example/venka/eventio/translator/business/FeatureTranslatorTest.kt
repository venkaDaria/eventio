package com.example.venka.eventio.translator.business

import com.example.venka.eventio.data.dto.business.FeatureDto
import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.support.assertEquals
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class FeatureTranslatorTest {

    @InjectMockKs
    private lateinit var featureTranslator: FeatureTranslator

    private val feature = Feature(id = "1", name = "has projector")
    private val featureDto = FeatureDto(id = "1", name = "has projector")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testToEntity() {
        val actual = featureTranslator.fromDto(featureDto, feature.id)

        feature.assertEquals(actual)
    }

    @Test
    fun testToEntity_NullId() {
        val actual = featureTranslator.fromDto(featureDto)

        feature.assertEquals(actual)
    }

    @Test
    fun testFromEntity() {
        val actual = featureTranslator.toDto(feature)

        featureDto.assertEquals(actual)
    }
}
