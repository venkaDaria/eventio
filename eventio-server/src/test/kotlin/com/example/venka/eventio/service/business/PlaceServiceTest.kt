package com.example.venka.eventio.service.business

import com.example.venka.eventio.data.db.business.PlaceRepository
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.business.impl.PlaceServiceImpl
import com.example.venka.eventio.support.assertEquals
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.util.Optional

class PlaceServiceTest {

    @InjectMockKs
    private lateinit var placeService: PlaceServiceImpl

    @MockK
    private lateinit var placeRepository: PlaceRepository

    private val place: Place = Place("1", "hello", "hello")
    private val place2: Place = Place("2", "hello", "hello 2")

    private val places = listOf(place, place2)

    private val roomId = "1"

    private val room = Room(roomId, "hello")

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        place.rooms.add(room)
    }

    @Test
    fun testGetAll() {
        every { placeRepository.findAll(2) } returns places

        assertEquals(placeService.getAll(), places)
    }

    @Test
    fun testSave() {
        every { placeRepository.save(place, 2) } returns place

        placeService.save(place)
    }

    @Test
    fun testGetById() {
        every { placeRepository.findById(place.id, 2) } returns Optional.of(place)
        every { placeRepository.findById(place2.id, 2) } returns Optional.of(place2)

        assertEquals(placeService.getById(place.id!!), place)
        assertEquals(placeService.getById(place2.id!!), place2)
    }

    @Test
    fun testGetByRoomId() {
        every { placeRepository.findByRoomsId(room.id!!) } returns place

        assertEquals(placeService.getByRoomId(room.id!!), place)
    }

    @Test
    fun testGetByParam() {
        every { placeRepository.findByRealAddress(place.realAddress) } returns place
        every { placeRepository.findByRealAddress(place2.realAddress) } returns place2

        assertEquals(placeService.getByParam(place.realAddress), place)
        assertEquals(placeService.getByParam(place2.realAddress), place2)
    }

    @Test
    fun testDeleteById() {
        every { placeRepository.deleteByIdWithChildren(place2.id!!) } answers { }

        placeService.deleteById(place2.id!!)
    }

    @Test
    fun testDeleteAll() {
        every { placeRepository.deleteAll() } answers { }

        placeService.deleteAll()
    }

    @Test
    fun testGetOwner() {
        val person = LegalPerson(id = "1", email = "hello")
        every { placeRepository.findOwner(room.id!!) } returns person

        val foundPerson = placeService.getOwner(room.id!!)

        foundPerson.assertEquals(person)
    }
}
