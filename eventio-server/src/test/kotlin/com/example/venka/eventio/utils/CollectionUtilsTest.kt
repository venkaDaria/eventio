package com.example.venka.eventio.utils

import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class CollectionUtilsTest {

    private val arrayList = arrayListOf("hello", "hi")

    @Test
    fun testDeepEquals() {
        assertTrue(arrayList eq arrayList)
    }

    @Test
    fun testDeepEquals_Null() {
        assertFalse(ArrayList<String>() eq null)
    }
}