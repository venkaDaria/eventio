package com.example.venka.eventio.translator.person

import com.example.venka.eventio.data.dto.person.NaturalPersonDto
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.translator.EventTranslator
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK

import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class NaturalPersonTranslatorTest {

    @InjectMockKs
    private lateinit var naturalPersonTranslator: NaturalPersonTranslator

    @MockK
    private lateinit var eventTranslator: EventTranslator

    private val naturalPerson = NaturalPerson(id = "1", email = "hello@gmail.com")
    private val naturalPersonDto = NaturalPersonDto(email = "hello@gmail.com")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testToEntity() {
        val actual = naturalPersonTranslator.fromDto(naturalPersonDto, naturalPerson.id)

        assertEquals(actual.toString(), naturalPerson.toString())
    }

    @Test
    fun testFromEntity() {
        val actual = naturalPersonTranslator.toDto(naturalPerson)

        assertEquals(actual, naturalPersonDto)
    }
}
