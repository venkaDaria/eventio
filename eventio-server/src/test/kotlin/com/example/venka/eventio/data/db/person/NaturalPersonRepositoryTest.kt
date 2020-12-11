package com.example.venka.eventio.data.db.person

import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.service.person.NaturalPersonService
import com.example.venka.eventio.service.person.impl.NaturalPersonServiceImpl
import com.example.venka.eventio.support.db.person.BootstrapNaturalPerson
import com.example.venka.eventio.support.db.person.naturalPerson
import com.example.venka.eventio.support.db.person.naturalPerson2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@DataNeo4jTest
class NaturalPersonRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var naturalPersonRepository: NaturalPersonRepository

    private lateinit var naturalPersonService: NaturalPersonService

    private val naturalPersons = listOf(naturalPerson, naturalPerson2)

    val naturalPerson3: NaturalPerson = NaturalPerson("3", "hello_3@gmail.com", "789")

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapNaturalPerson(naturalPersonRepository)
        bootstrapPerson.run()

        naturalPersonService = NaturalPersonServiceImpl(naturalPersonRepository)
    }

    @Test
    fun testGetAll() {
        assertEquals(naturalPersonService.getAll(), naturalPersons)
    }

    @Test
    fun testSave() {
        naturalPersonService.save(naturalPerson3)

        assertEquals(naturalPersonService.getAll(), listOf(naturalPerson, naturalPerson2, naturalPerson3))
    }

    @Test
    fun testGetById() {
        assertEquals(naturalPersonService.getById(naturalPerson.id!!), naturalPerson)
        assertEquals(naturalPersonService.getById(naturalPerson2.id!!), naturalPerson2)
    }

    @Test
    fun testDeleteById() {
        naturalPersonService.deleteById(naturalPerson2.id!!)

        assertEquals(naturalPersonService.getAll(), listOf(naturalPerson))
    }

    @Test
    fun testGetByParam() {
        assertEquals(naturalPersonService.getByParam(naturalPerson.email), naturalPerson)
        assertEquals(naturalPersonService.getByParam(naturalPerson2.email), naturalPerson2)
    }

    @Test
    fun testDeleteAll() {
        naturalPersonService.deleteAll()

        assertEquals(naturalPersonService.getAll(), emptyList<Person>())
    }
}

