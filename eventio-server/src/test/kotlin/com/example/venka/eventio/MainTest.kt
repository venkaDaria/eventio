package com.example.venka.eventio

import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test

@DataNeo4jTest
class MainTest : AbstractTestNGSpringContextTests() {

    @Test
    fun test() {
        assertNotNull(EventioApplication())

        assertNotNull(LoggingImpl.invoke<MainTest>())

        main(arrayOf())
    }
}
