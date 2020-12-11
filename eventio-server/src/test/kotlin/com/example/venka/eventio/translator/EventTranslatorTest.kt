package com.example.venka.eventio.translator

import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.translator.business.RoomTranslator
import com.example.venka.eventio.utils.format.formatToString
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.time.LocalDateTime

class EventTranslatorTest {

    @InjectMockKs
    private lateinit var eventTranslator: EventTranslator

    @MockK
    private lateinit var roomTranslator: RoomTranslator

    private val date = LocalDateTime.now().withNano(0)

    private val event = Event(id = "1", title = "Event #1", start = date, location = Room())
    private val eventDto = EventDto(id = "1", title = "Event #1", start = date.formatToString(), location = RoomDto())

    private val event2 = Event(id = "1", title = "Event #1", start = date, end = date.plusHours(2),
            location = Room())
    private val eventDto2 = EventDto(id = "1", title = "Event #1", start = date.formatToString(),
            end = date.plusHours(2).formatToString(), location = RoomDto())

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        every { roomTranslator.toDto(any()) } returns RoomDto()
        every { roomTranslator.fromDto(any()) } returns Room()
    }

    @Test
    fun testToEntity() {
        val actual = eventTranslator.fromDto(eventDto, event.id)

        event.assertEquals(actual)
    }

    @Test
    fun testToEntity_NullId() {
        val actual = eventTranslator.fromDto(eventDto)

        event.assertEquals(actual)
    }

    @Test
    fun testFromEntity() {
        val actual = eventTranslator.toDto(event)

        eventDto.assertEquals(actual)
    }

    @Test
    fun testToEntity_WithEnd() {
        val actual = eventTranslator.fromDto(eventDto2, event2.id)

        event2.assertEquals(actual)
    }

    @Test
    fun testFromEntity_WithEnd() {
        val actual = eventTranslator.toDto(event2)

        eventDto2.assertEquals(actual)
    }
}
