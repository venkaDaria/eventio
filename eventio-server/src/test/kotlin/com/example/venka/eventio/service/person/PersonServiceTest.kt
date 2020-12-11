package com.example.venka.eventio.service.person

import com.example.venka.eventio.config.security.PersonAuthority.Companion.legal
import com.example.venka.eventio.config.security.PersonAuthority.Companion.natural
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.db.person.PersonRepository
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.NaturalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.PersonServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class PersonServiceTest {

    @InjectMockKs
    private lateinit var personService: PersonServiceImpl

    @MockK
    private lateinit var personRepository: PersonRepository

    @MockK
    private lateinit var naturalPersonService: NaturalPersonServiceImpl

    @MockK
    private lateinit var legalPersonService: LegalPersonServiceImpl

    private val person = NaturalPerson("1", "hello@gmail.com", "123")
    private val person2 = LegalPerson("2", "hello_2@gmail.com", "456")

    private val persons = listOf(person, person2)

    private val principalNatural = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails(person.email, person.location), null, natural)

    private val principalLegal = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails(person2.email), null, legal)

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetAll() {
        every { personRepository.findAll(2) } returns persons

        assertEquals(personService.getAll(), persons)
    }

    @Test
    fun testSave() {
        every { personRepository.save(person, 2) } returns person

        personService.save(person)
    }

    @Test
    fun testGetById() {
        every { naturalPersonService.getById(person.id!!) } returns person
        every { naturalPersonService.getById(person2.id!!) } returns null
        every { legalPersonService.getById(person2.id!!) } returns person2

        assertEquals(personService.getById(person.id!!), person)
        assertEquals(personService.getById(person2.id!!), person2)
    }

    @Test
    fun testDeleteById() {
        every { naturalPersonService.deleteById(person2.id!!) } answers { }
        every { legalPersonService.deleteById(person2.id!!) } answers { }

        personService.deleteById(person2.id!!)
    }

    @Test
    fun testGetByParam() {
        every { naturalPersonService.getByParam(person.email) } returns person
        every { naturalPersonService.getByParam(person2.email) } returns null
        every { legalPersonService.getByParam(person2.email) } returns person2

        assertEquals(personService.getByParam(person.email), person)
        assertEquals(personService.getByParam(person2.email), person2)
    }

    @Test
    fun testDeleteAll() {
        every { personRepository.deleteAll() } answers { }

        personService.deleteAll()
    }

    @Test
    fun testToggleLabel_NaturalPerson() {
        every { personRepository.setLegalPersonLabel(principalNatural.name) } answers { }

        personService.toggleLabel(principalNatural)
    }

    @Test
    fun testToggleLabel_LegalPerson() {
        every { personRepository.setNaturalPersonLabel(principalLegal.name) } answers { }

        personService.toggleLabel(principalLegal)
    }
}

