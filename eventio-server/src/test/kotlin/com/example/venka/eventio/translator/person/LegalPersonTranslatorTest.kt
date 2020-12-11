package com.example.venka.eventio.translator.person

import com.example.venka.eventio.data.dto.person.LegalPersonDto
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.business.PlaceTranslator
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK

import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class LegalPersonTranslatorTest {

    @InjectMockKs
    private lateinit var legalPersonTranslator: LegalPersonTranslator

    @MockK
    private lateinit var placeTranslator: PlaceTranslator

    @MockK
    private lateinit var eventTranslator: EventTranslator

    private val legalPerson = LegalPerson(id = "1", email = "hello@gmail.com", _name = "Company")
    private val legalPersonDto = LegalPersonDto(email = "hello@gmail.com", name = "Company", url = "company-company")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testToEntity() {
        val actual = legalPersonTranslator.fromDto(legalPersonDto, legalPerson.id)

        legalPerson.assertEquals(actual)
    }

    @Test
    fun testFromEntity() {
        val actual = legalPersonTranslator.toDto(legalPerson)

        legalPersonDto.assertEquals(actual)
    }
}
