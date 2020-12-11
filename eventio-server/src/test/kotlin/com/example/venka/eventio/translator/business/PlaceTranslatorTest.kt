package com.example.venka.eventio.translator.business

import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.support.assertEquals
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class PlaceTranslatorTest {

    @InjectMockKs
    private lateinit var placeTranslator: PlaceTranslator

    @MockK
    private lateinit var roomTranslator: RoomTranslator

    private val place = Place(id = "1", realAddress = "Purple avenue", timeWork = null, image = null)
    private val placeDto = PlaceDto(id = "1", realAddress = "Purple avenue", timeWork = null, image = null)

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testToEntity() {
        val actual = placeTranslator.fromDto(placeDto, place.id)

        place.assertEquals(actual)
    }

    @Test
    fun testToEntity_NullId() {
        val actual = placeTranslator.fromDto(placeDto)

        place.assertEquals(actual)
    }

    @Test
    fun testFromEntity() {
        val actual = placeTranslator.toDto(place)

        placeDto.assertEquals(actual)
    }
}
