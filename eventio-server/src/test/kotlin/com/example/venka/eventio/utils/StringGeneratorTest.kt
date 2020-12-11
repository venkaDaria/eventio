package com.example.venka.eventio.utils

import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class StringGeneratorTest {

    private val regex = Regex("^[a-z]{7}$")

    @Test
    fun testGetUrlString() {
        val randomString = getUrlString()

        println(randomString)

        assertTrue(randomString.length == 7)
        assertTrue(randomString.matches(regex))
    }
}
