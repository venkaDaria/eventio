package com.example.venka.eventio.controller

import com.example.venka.eventio.support.ERROR_UNEXPECTED
import com.example.venka.eventio.support.assertEquals
import com.example.venka.eventio.support.createMockMvc
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

class GlobalExceptionHandlerTest {

    private lateinit var mockMvc: MockMvc

    @MockK
    private lateinit var loginController: LoginController

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = loginController.createMockMvc()
    }

    @Test
    @Throws(RuntimeException::class)
    fun testGlobalExceptionHandlerError() {
        every { loginController.login() } throws RuntimeException(ERROR_UNEXPECTED.message)

        val result = mockMvc.perform(get("/login"))
                .andExpect(status().`is`(500))
                .andExpect(jsonPath("$.*", hasSize<Collection<*>>(2)))
                .andExpect(jsonPath("$.code", equalTo(ERROR_UNEXPECTED.code.name)))
                .andExpect(jsonPath("$.message", equalTo(ERROR_UNEXPECTED.message)))
                .andReturn()

        result.assertEquals(ERROR_UNEXPECTED)
    }
}