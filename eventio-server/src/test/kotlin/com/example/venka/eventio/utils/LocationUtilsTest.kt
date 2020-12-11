package com.example.venka.eventio.utils

import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.dto.business.RoomDto
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class LocationUtilsTest {

    private val dto = RoomDto()

    private val place = PlaceDto(name = "Парк им. Горького", realAddress = "Центральный парк культуры и отдыха им. " +
            "М. Горького, Sumska Street, Kharkiv, Харьковская область, Ukraine")

    private val place2 = PlaceDto(name = "Мой дом", realAddress = "77, проспект Перемоги, Харків, " +
            "Харківська область, Ukraine")

    private val place3 = PlaceDto(name = "Дергачи", realAddress = "Дергачи, Харьковская область, Ukraine")

    private val place4 = PlaceDto(name = "Антикафе \"Коллаж\"", realAddress = "Антикафе \"Коллаж\", " +
            "Knyazhyi Zaton Street, Киев, Ukraine")

    @Test
    fun testNear() {
        assertTrue(place.near(place2.realAddress))
    }

    @Test
    fun testNear_theSame() {
        assertTrue(place.near(place.realAddress))
    }

    @Test
    fun testNear_False() {
        assertFalse(place.near(place3.realAddress))
    }

    @Test
    fun testNear_False_Kiev() {
        assertFalse(place.near(place4.realAddress))
    }

    @Test
    fun testNear_NotExists() {
        assertFalse(place.near("wrong wrong location"))
    }

    @Test
    fun testNear_NotExists_Place() {
        assertFalse(PlaceDto(realAddress = "wrong wrong location").near(place.realAddress))
    }
}