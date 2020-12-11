package com.example.venka.eventio.config.aop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

@DataNeo4jTest
@EnableAspectJAutoProxy
@Import(LoggingAspect::class)
class LoggingAspectTest : AbstractTestNGSpringContextTests() {

    @Monitor
    @Component
    class TestClassForLoggingAspect {

        fun sayHello(): String {
            return "hello"
        }

        @Throws(RuntimeException::class)
        fun throwException() {
            throw RuntimeException()
        }
    }

    @Autowired
    private lateinit var testClassForLoggingAspect: TestClassForLoggingAspect

    @Test
    fun testSomething() {
        val hello = testClassForLoggingAspect.sayHello()

        assertEquals(hello, "hello")
    }

    @Test(expectedExceptions = [RuntimeException::class])
    fun testExceptionThrowing() {
        testClassForLoggingAspect.throwException()
    }
}