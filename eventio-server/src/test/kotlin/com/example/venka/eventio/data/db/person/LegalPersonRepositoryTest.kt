package com.example.venka.eventio.data.db.person

import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import com.example.venka.eventio.support.db.person.BootstrapLegalPerson
import com.example.venka.eventio.support.db.person.legalPerson
import com.example.venka.eventio.support.db.person.legalPerson2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@DataNeo4jTest
class LegalPersonRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var legalPersonRepository: LegalPersonRepository

    private lateinit var legalPersonService: LegalPersonService

    private val legalPersons = listOf(legalPerson, legalPerson2)

    val legalPerson3: LegalPerson = LegalPerson("3", "hello_3@gmail.com", "789")

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapLegalPerson(legalPersonRepository)
        bootstrapPerson.run()

        legalPersonService = LegalPersonServiceImpl(legalPersonRepository)
    }

    @Test
    fun testGetAll() {
        assertEquals(legalPersonService.getAll(), legalPersons)
    }

    @Test
    fun testSave() {
        legalPersonService.save(legalPerson3)

        assertEquals(legalPersonService.getAll(), listOf(legalPerson, legalPerson2, legalPerson3))
    }

    @Test
    fun testGetById() {
        assertEquals(legalPersonService.getById(legalPerson.id!!), legalPerson)
        assertEquals(legalPersonService.getById(legalPerson2.id!!), legalPerson2)
    }

    @Test
    fun testDeleteById() {
        legalPersonService.deleteById(legalPerson2.id!!)

        assertEquals(legalPersonService.getAll(), listOf(legalPerson))
    }

    @Test
    fun testGetByParam() {
        assertEquals(legalPersonService.getByParam(legalPerson.email), legalPerson)
        assertEquals(legalPersonService.getByParam(legalPerson2.email), legalPerson2)
    }

    @Test
    fun testDeleteAll() {
        legalPersonService.deleteAll()

        assertEquals(legalPersonService.getAll(), emptyList<Person>())
    }
}
