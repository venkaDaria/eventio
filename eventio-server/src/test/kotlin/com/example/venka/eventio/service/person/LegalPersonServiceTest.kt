package com.example.venka.eventio.service.person

import com.example.venka.eventio.data.db.person.LegalPersonRepository
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotEquals
import org.testng.Assert.assertNotNull
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.util.Optional

class LegalPersonServiceTest {

    @InjectMockKs
    private lateinit var legalPersonService: LegalPersonServiceImpl

    @MockK
    private lateinit var legalPersonRepository: LegalPersonRepository

    private val legalPerson: LegalPerson = LegalPerson("1", "hello@gmail.com", "123")
    private val legalPerson2: LegalPerson = LegalPerson("2", "hello_2@gmail.com", "456")

    private val legalPersons = listOf(legalPerson, legalPerson2)

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetAll() {
        every { legalPersonRepository.findAll(2) } returns legalPersons

        assertEquals(legalPersonService.getAll(), legalPersons)
    }

    @Test
    fun testSave() {
        every { legalPersonRepository.save(legalPerson, 2) } returns legalPerson

        legalPersonService.save(legalPerson)
    }

    @Test
    fun testGetById() {
        every { legalPersonRepository.findById(legalPerson.id, 2) } returns Optional.of(legalPerson)
        every { legalPersonRepository.findById(legalPerson2.id, 2) } returns Optional.of(legalPerson2)

        assertEquals(legalPersonService.getById(legalPerson.id!!), legalPerson)
        assertEquals(legalPersonService.getById(legalPerson2.id!!), legalPerson2)
    }

    @Test
    fun testDeleteById() {
        every { legalPersonRepository.deleteById(legalPerson2.id!!) } answers { }

        legalPersonService.deleteById(legalPerson2.id!!)
    }

    @Test
    fun testGetByParam() {
        every { legalPersonRepository.findByEmail(legalPerson.email) } returns legalPerson
        every { legalPersonRepository.findByEmail(legalPerson2.email) } returns legalPerson2

        assertEquals(legalPersonService.getByParam(legalPerson.email), legalPerson)
        assertEquals(legalPersonService.getByParam(legalPerson2.email), legalPerson2)
    }

    @Test
    fun testDeleteAll() {
        every { legalPersonRepository.deleteAll() } answers { }

        legalPersonService.deleteAll()
    }

    @Test
    fun testGetByCompanyUrl() {
        every { legalPersonRepository.findByUrl(legalPerson.url) } returns legalPerson

        assertEquals(legalPersonService.getByCompanyUrl(legalPerson.url), legalPerson)
    }

    @Test
    fun testFixCompanyUrl() {
        val oldUrl = legalPerson.url
        every { legalPersonRepository.findByUrl(legalPerson.url) } returns legalPerson
        every { legalPersonRepository.findByUrl(not(legalPerson.url)) } returns null

        val newLegalPerson = legalPersonService.fixCompanyUrl(legalPerson)

        assertNotNull(newLegalPerson.name)
        assertNotNull(newLegalPerson.url)
        assertEquals(newLegalPerson.url, legalPerson.url)
        assertNotEquals(newLegalPerson.url, oldUrl)

        legalPerson.url = oldUrl
    }

    @Test
    fun testFixCompanyUrl_Null() {
        val oldUrl = legalPerson.url
        legalPerson.name = ""

        every { legalPersonRepository.findByUrl(legalPerson.url) } returns null

        val newLegalPerson = legalPersonService.fixCompanyUrl(legalPerson)

        assertNotNull(newLegalPerson.name)
        assertNotNull(newLegalPerson.url)
        assertEquals(newLegalPerson.url, legalPerson.url)
        assertNotEquals(newLegalPerson.url, oldUrl)

        legalPerson.url = oldUrl
    }
}
