package com.example.venka.eventio.service.person

import com.example.venka.eventio.data.db.person.NaturalPersonRepository
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.service.person.impl.NaturalPersonServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.util.Optional

class NaturalPersonServiceTest {

    @InjectMockKs
    private lateinit var naturalPersonService: NaturalPersonServiceImpl

    @MockK
    private lateinit var naturalPersonRepository: NaturalPersonRepository

    private val naturalPerson: NaturalPerson = NaturalPerson("1", "hello@gmail.com", "123")
    private val naturalPerson2: NaturalPerson = NaturalPerson("2", "hello_2@gmail.com", "456")

    private val naturalPersons = listOf(naturalPerson, naturalPerson2)

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetAll() {
        every { naturalPersonRepository.findAll(2) } returns naturalPersons

        assertEquals(naturalPersonService.getAll(), naturalPersons)
    }

    @Test
    fun testSave() {
        every { naturalPersonRepository.save(naturalPerson, 2) } returns naturalPerson

        naturalPersonService.save(naturalPerson)
    }

    @Test
    fun testGetById() {
        every { naturalPersonRepository.findById(naturalPerson.id, 2) } returns Optional.of(naturalPerson)
        every { naturalPersonRepository.findById(naturalPerson2.id, 2) } returns Optional.of(naturalPerson2)

        assertEquals(naturalPersonService.getById(naturalPerson.id!!), naturalPerson)
        assertEquals(naturalPersonService.getById(naturalPerson2.id!!), naturalPerson2)
    }

    @Test
    fun testDeleteById() {
        every { naturalPersonRepository.deleteById(naturalPerson2.id!!) } answers { }

        naturalPersonService.deleteById(naturalPerson2.id!!)
    }

    @Test
    fun testGetByParam() {
        every { naturalPersonRepository.findByEmail(naturalPerson.email) } returns naturalPerson
        every { naturalPersonRepository.findByEmail(naturalPerson2.email) } returns naturalPerson2

        assertEquals(naturalPersonService.getByParam(naturalPerson.email), naturalPerson)
        assertEquals(naturalPersonService.getByParam(naturalPerson2.email), naturalPerson2)
    }

    @Test
    fun testDeleteAll() {
        every { naturalPersonRepository.deleteAll() } answers { }

        naturalPersonService.deleteAll()
    }
}

