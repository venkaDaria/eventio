package com.example.venka.eventio.data.db.business

import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.business.impl.RoomServiceImpl
import com.example.venka.eventio.support.assertEqualsCollection
import com.example.venka.eventio.support.db.business.BootstrapRoom
import com.example.venka.eventio.support.db.business.room
import com.example.venka.eventio.support.db.business.room2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@DataNeo4jTest
class RoomRepositoryTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var roomRepository: RoomRepository

    private lateinit var roomService: RoomServiceImpl

    private val room3: Room = Room("3", "hello 3")

    private val rooms = listOf(room, room2)

    @BeforeMethod
    fun setUp() {
        val bootstrapPerson = BootstrapRoom(roomRepository)
        bootstrapPerson.run()

        roomService = RoomServiceImpl(roomRepository)
    }

    @Test
    fun testGetAll() {
        roomService.getAll().assertEqualsCollection<Room, String>(rooms)
    }

    @Test
    fun testSave() {
        roomService.save(room3)

        roomService.getAll().assertEqualsCollection<Room, String>(listOf(room, room2, room3))
    }

    @Test
    fun testGetById() {
        assertEquals(roomService.getById(room.id!!), room)
        assertEquals(roomService.getById(room2.id!!), room2)
    }

    @Test
    fun testDeleteById() {
        roomService.deleteById(room2.id!!)

        assertEquals(roomService.getAll(), listOf(room))
    }

    @Test(expectedExceptions = [UnsupportedOperationException::class])
    fun testGetByParam() {
        roomService.getByParam(room.name)
    }

    @Test
    fun testDeleteAll() {
        roomService.deleteAll()

        assertEquals(roomService.getAll(), emptyList<Room>())
    }
}
