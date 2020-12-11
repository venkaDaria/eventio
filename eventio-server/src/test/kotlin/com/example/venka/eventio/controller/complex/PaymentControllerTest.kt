package com.example.venka.eventio.controller.complex

import com.example.venka.eventio.config.security.PersonAuthority.Companion.legal
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.support.createMockMvc
import com.liqpay.LiqPay
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class PaymentControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMockKs
    private lateinit var paymentController: PaymentController

    @MockK
    private lateinit var liqPay: LiqPay

    private val principal = UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello"), null, legal
    )

    @BeforeClass
    fun setUp() {
        MockKAnnotations.init(this)

        mockMvc = paymentController.createMockMvc()
    }

    @Test
    fun testGetForm() {
        val expected = "form example"

        every { liqPay.cnb_form(any<HashMap<String, String>>()) } returns expected

        val result = mockMvc.perform(get("/payment-form")
                .principal(principal)
                .param("amount", "10")
                .param("text", "text")
                .param("resultUrl", "result_url"))
                .andExpect(status().`is`(200))
                .andExpect(content().string(expected))
                .andReturn()

        assertEquals(result.response.contentAsString, expected)
    }
}
