package com.example.venka.eventio.controller

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.view.InternalResourceView
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class LoginControllerContextTest {

    @InjectMockKs
    private lateinit var loginController: LoginController

    private lateinit var mockMvc: MockMvc

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setSingleView(InternalResourceView("/templates/login.html"))
                .build()
    }

    @Test
    fun testLoginPage() {
        mockMvc.perform(get("/login"))
                .andExpect(status().`is`(200))
                .andExpect(view().name("login"))
    }

    @Test
    fun testSwaggerRedirect() {
        mockMvc.perform(get("/"))
                .andExpect(status().`is`(200))
    }
}