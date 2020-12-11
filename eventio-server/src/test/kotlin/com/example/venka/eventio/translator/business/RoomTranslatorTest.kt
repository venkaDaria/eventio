package com.example.venka.eventio.translator.business

import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.support.assertEquals
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class RoomTranslatorTest {

    @InjectMockKs
    private lateinit var roomTranslator: RoomTranslator

    @MockK
    private lateinit var featureTranslator: FeatureTranslator

    private val room = Room(id = "1", name = "Cool location")
    private val roomDto = RoomDto(id = "1", name = "Cool location")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testToEntity() {
        val actual = roomTranslator.fromDto(roomDto, room.id)

        room.assertEquals(actual)
    }

    @Test
    fun testToEntity_NullId() {
        val actual = roomTranslator.fromDto(roomDto)

        room.assertEquals(actual)
    }

    @Test
    fun testFromEntity() {
        val actual = roomTranslator.toDto(room)

        roomDto.assertEquals(actual)
    }
}
