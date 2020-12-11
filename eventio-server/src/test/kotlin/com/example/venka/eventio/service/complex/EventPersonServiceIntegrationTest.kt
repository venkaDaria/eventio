package com.example.venka.eventio.service.complex

import com.example.venka.eventio.service.complex.impl.EventPersonService
import com.example.venka.eventio.service.impl.EventServiceImpl
import com.example.venka.eventio.service.person.impl.LegalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.NaturalPersonServiceImpl
import com.example.venka.eventio.service.person.impl.PersonServiceImpl
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.db.complex.BootstrapEventPerson
import com.example.venka.eventio.support.db.complex.event
import com.example.venka.eventio.support.db.complex.naturalPerson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

@DataNeo4jTest
@Import(
        EventServiceImpl::class,
        NaturalPersonServiceImpl::class,
        LegalPersonServiceImpl::class,
        PersonServiceImpl::class
)
class EventPersonServiceIntegrationTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var personService: PersonServiceImpl

    @Autowired
    private lateinit var naturalPersonService: NaturalPersonServiceImpl

    @Autowired
    private lateinit var legalPersonService: LegalPersonServiceImpl

    @Autowired
    private lateinit var eventService: EventServiceImpl

    private lateinit var eventPersonService: EventPersonService

    @BeforeClass
    fun setUp() {
        val bootstrapPerson = BootstrapEventPerson(naturalPersonService, legalPersonService)
        bootstrapPerson.run()

        eventPersonService = EventPersonService(personService, naturalPersonService, legalPersonService, eventService)
    }

    @Test
    fun testCreate() {
        val actual = eventPersonService.create(event, naturalPerson.email)

        val naturalPerson = naturalPersonService.getByParam(naturalPerson.email)

        actual!!.assertEquals(naturalPerson)
    }
}
