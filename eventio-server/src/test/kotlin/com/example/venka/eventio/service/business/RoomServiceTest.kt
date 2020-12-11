package com.example.venka.eventio.service.business

import com.example.venka.eventio.data.db.business.RoomRepository
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.business.impl.RoomServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.util.Optional

class RoomServiceTest {

    @InjectMockKs
    private lateinit var roomService: RoomServiceImpl

    @MockK
    private lateinit var roomRepository: RoomRepository

    private val room: Room = Room("1", "hello")
    private val room2: Room = Room("2", "hello 2")

    private val rooms = listOf(room, room2)

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetAll() {
        every { roomRepository.findAll(2) } returns rooms

        assertEquals(roomService.getAll(), rooms)
    }

    @Test
    fun testSave() {
        every { roomRepository.save(room, 2) } returns room

        roomService.save(room)
    }

    @Test
    fun testGetById() {
        every { roomRepository.findById(room.id, 2) } returns Optional.of(room)
        every { roomRepository.findById(room2.id, 2) } returns Optional.of(room2)

        assertEquals(roomService.getById(room.id!!), room)
        assertEquals(roomService.getById(room2.id!!), room2)
    }

    @Test
    fun testDeleteById() {
        every { roomRepository.deleteByIdWithChildren(room2.id!!) } answers { }

        roomService.deleteById(room2.id!!)
    }

    @Test(expectedExceptions = [UnsupportedOperationException::class])
    fun testGetByParam() {
        roomService.getByParam(room.name)
    }

    @Test
    fun testDeleteAll() {
        every { roomRepository.deleteAll() } answers { }

        roomService.deleteAll()
    }
}

