package com.example.venka.eventio.data.model

import com.example.venka.eventio.data.model.business.Feature
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class FeatureTest {

    private val name = "hello"

    private val feature = Feature(name = name)

    @Test
    fun testGetParam() {
        assertEquals(feature.getParam(), name)
    }
}
