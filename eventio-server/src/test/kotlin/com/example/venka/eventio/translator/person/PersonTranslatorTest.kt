package com.example.venka.eventio.translator.person

import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.dto.person.PersonDto
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.translator.EventTranslator
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class PersonTranslatorTest {

    @InjectMockKs
    private lateinit var personTranslator: PersonTranslator

    @MockK
    private lateinit var eventTranslator: EventTranslator

    private val event = Event("1", "hello", location = Room())
    private val event2 = Event("2", "hello 2", location = Room())

    private val eventDto = EventDto("1", "hello", location = RoomDto())
    private val eventDto2 = EventDto("2", "hello 2", location = RoomDto())

    private val person = NaturalPerson("1", "hello@gmail.com", createdEvents = mutableSetOf(event),
            subscribedEvents = mutableSetOf(event2))

    private val person2 = LegalPerson("2", "hello2@gmail.com", createdEvents = mutableSetOf(event2))

    private val person3 = NaturalPerson("3", "hello3@gmail.com", createdEvents = mutableSetOf(event))

    private val personDto = PersonDto("hello@gmail.com", createdEvents = mutableSetOf(eventDto),
            subscribedEvents = mutableSetOf(eventDto2))

    private val personDto2 = PersonDto("hello2@gmail.com", createdEvents = mutableSetOf(eventDto2))

    private val personDto3 = PersonDto("hello3@gmail.com", createdEvents = mutableSetOf(eventDto))

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        every { eventTranslator.toDto(event) } returns eventDto
        every { eventTranslator.toDto(event2) } returns eventDto2
        every { eventTranslator.fromDto(eventDto) } returns event
        every { eventTranslator.fromDto(eventDto2) } returns event2
    }

    @Test
    fun testToEntity() {
        val actual = personTranslator.fromDto(personDto, person.id)

        actual.assertEquals(person)
    }

    @Test(expectedExceptions = [NullPointerException::class])
    fun testToEntity_NullId() {
        personTranslator.fromDto(personDto)
    }

    @Test
    fun testFromEntity_NaturalPerson() {
        val actual = personTranslator.toDto(person)

        actual.assertEquals(personDto)
    }

    @Test
    fun testFromEntity_NaturalPerson_NullEvents() {
        val actual = personTranslator.toDto(person3)

        actual.assertEquals(personDto3)
    }

    @Test
    fun testFromEntity_LegalPerson() {
        val actual = personTranslator.toDto(person2)

        actual.assertEquals(personDto2)
    }
}
