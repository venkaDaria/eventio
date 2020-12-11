package com.example.venka.eventio.data.model

import com.example.venka.eventio.data.model.business.Room
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class RoomTest {

    private val name = "hello"

    private val room = Room(name = name)

    @Test
    fun testGetParam() {
        assertEquals(room.getParam(), name)
    }
}
