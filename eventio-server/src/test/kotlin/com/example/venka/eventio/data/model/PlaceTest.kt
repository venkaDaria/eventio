package com.example.venka.eventio.data.model

import com.example.venka.eventio.data.model.business.Place
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class PlaceTest {

    private val realAddress = "address"

    private val place = Place(realAddress = realAddress)

    @Test
    fun testGetParam() {
        assertEquals(place.getParam(), realAddress)
    }
}
