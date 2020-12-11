package com.example.venka.eventio.data.db.business

import com.example.venka.eventio.data.db.person.LegalPersonRepository
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.service.business.impl.PlaceServiceImpl
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.assertEqualsCollection
import com.example.venka.eventio.support.db.business.BootstrapPlace
import com.example.venka.eventio.support.db.business.person
import com.example.venka.eventio.support.db.business.place
import com.example.venka.eventio.support.db.business.place2
import com.example.venka.eventio.support.db.business.roomId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@DataNeo4jTest
class PlaceRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var personRepository: LegalPersonRepository

    private lateinit var placeService: PlaceServiceImpl

    private val place3: Place = Place("3", "hello 3", "hello 3")

    private val places = listOf(place, place2)

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapPlace(placeRepository, personRepository)
        bootstrapPerson.run()

        placeService = PlaceServiceImpl(placeRepository)
    }

    @Test
    fun testGetAll() {
        assertEquals(placeService.getAll(), places)
    }

    @Test
    fun testSave() {
        placeService.save(place3)

        placeService.getAll().assertEqualsCollection<Place, String>(listOf(place, place2, place3))
    }

    @Test
    fun testGetById() {
        assertEquals(placeService.getById(place.id!!), place)
        assertEquals(placeService.getById(place2.id!!), place2)
    }

    @Test
    fun testDeleteById() {
        placeService.deleteById(place2.id!!)

        assertEquals(placeService.getAll(), listOf(place))
    }

    @Test
    fun testGetByParam() {
        assertEquals(placeService.getByParam(place.realAddress), place)
        assertEquals(placeService.getByParam(place2.realAddress), place2)
    }

    @Test
    fun testDeleteAll() {
        placeService.deleteAll()

        assertEquals(placeService.getAll(), emptyList<Place>())
    }

    @Test
    fun testGetOwner() {
        val foundPerson = placeService.getOwner(roomId)

        foundPerson.assertEquals(person)
    }
}
