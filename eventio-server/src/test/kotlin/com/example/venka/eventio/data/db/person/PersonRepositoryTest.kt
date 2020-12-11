package com.example.venka.eventio.data.db.person

import com.example.venka.eventio.config.security.PersonAuthority.Companion.legal
import com.example.venka.eventio.config.security.PersonAuthority.Companion.natural
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.service.person.NaturalPersonService
import com.example.venka.eventio.service.person.PersonService
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.NaturalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.PersonServiceImpl
import com.example.venka.eventio.support.db.person.BootstrapPerson
import com.example.venka.eventio.support.db.person.person
import com.example.venka.eventio.support.db.person.person2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@DataNeo4jTest
@Import(
        LegalPersonServiceImpl::class,
        NaturalPersonServiceImpl::class
)
class PersonRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var personRepository: PersonRepository

    @Autowired
    private lateinit var legalPersonService: LegalPersonService

    @Autowired
    private lateinit var naturalPersonService: NaturalPersonService

    private lateinit var personService: PersonService

    private val persons = listOf(person, person2)

    val person3 = LegalPerson("3", "hello_3@gmail.com", "789")

    private val principalNatural = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails(person.email, person.location), null, natural)

    private val principalLegal = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails(person2.email), null, legal)

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapPerson(personRepository)
        bootstrapPerson.run()

        personService = PersonServiceImpl(personRepository, legalPersonService, naturalPersonService)
    }

    @Test
    fun testGetAll() {
        assertEquals(personService.getAll(), persons)
    }

    @Test
    fun testSave() {
        personService.save(person3)

        assertEquals(personService.getAll(), listOf(person, person2, person3))
    }

    @Test
    fun testGetById() {
        assertEquals(personService.getById(person.id!!), person)
        assertEquals(personService.getById(person2.id!!), person2)
    }

    @Test
    fun testGetByParam() {
        assertEquals(personService.getByParam(person.email), person)
        assertEquals(personService.getByParam(person2.email), person2)
    }

    @Test
    fun testDeleteById() {
        personService.deleteById(person2.id!!)

        assertEquals(personService.getAll(), listOf(person))
    }

    @Test
    fun testDeleteAll() {
        personService.deleteAll()

        assertEquals(personService.getAll(), emptyList<Person>())
    }

    @Test
    fun testToggleLabel_NaturalPerson() {
        personService.toggleLabel(principalNatural)

        assertTrue(personService.getByParam(person.email) is LegalPerson)
    }

    @Test
    fun testToggleLabel_LegalPerson() {
        personService.toggleLabel(principalLegal)

        assertTrue(personService.getByParam(person2.email) is NaturalPerson)
    }
}

