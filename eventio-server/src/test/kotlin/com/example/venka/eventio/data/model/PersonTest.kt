package com.example.venka.eventio.data.model

import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class PersonTest {

    private val email = "hello@gmail.com"

    private val naturalPerson = NaturalPerson(email = email)

    private val legalPerson = LegalPerson(email = "hello2@gmail.com", _name = "hello2")

    @Test
    fun testGetParam() {
        assertEquals(naturalPerson.getParam(), email)
    }

    @Test
    fun testSetName() {
        assertEquals(legalPerson.url, "company-hello2")

        legalPerson.name = "cool"
        assertEquals(legalPerson.url, "company-cool")
    }
}
