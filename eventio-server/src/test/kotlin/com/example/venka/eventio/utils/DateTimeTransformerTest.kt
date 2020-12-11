package com.example.venka.eventio.utils

import com.example.venka.eventio.utils.format.formatToString
import com.example.venka.eventio.utils.format.parse
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import java.time.LocalDateTime

class DateTimeTransformerTest {

    private val localDateTime: LocalDateTime = LocalDateTime.of(2018, 1, 1, 21, 21)

    private val localDateTimeAsString: String = "01/01/2018 21:21:00"

    @Test
    fun testToString() {
        assertEquals(localDateTime.formatToString(), localDateTimeAsString)
    }

    @Test
    fun testParse() {
        assertEquals(localDateTimeAsString.parse(), localDateTime)
    }
}