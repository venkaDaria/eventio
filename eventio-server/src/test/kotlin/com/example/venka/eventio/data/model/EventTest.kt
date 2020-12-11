package com.example.venka.eventio.data.model

import com.example.venka.eventio.data.model.business.Room
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class EventTest {

    private val title = "hello"

    private val event = Event(title = title, location = Room())

    @Test
    fun testGetParam() {
        assertEquals(event.getParam(), title)
    }
}
