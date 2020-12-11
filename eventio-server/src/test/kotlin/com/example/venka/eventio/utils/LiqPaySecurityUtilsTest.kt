package com.example.venka.eventio.utils

import com.example.venka.eventio.config.SpringConfiguration
import com.liqpay.LiqPayUtil.base64_encode
import com.liqpay.LiqPayUtil.sha1
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

@Component
@Import(LiqPaySecurityImpl::class, SpringConfiguration::class)
class LiqPaySecurityUtilsTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var liqPaySecurityImpl: LiqPaySecurityImpl

    @Autowired
    private lateinit var keyPrivate: String

    @Autowired
    private lateinit var keyPublic: String

    private val data = "data"

    private lateinit var signature: String

    @BeforeClass
    fun setUp() {
        signature = base64_encode(sha1(keyPrivate + data + keyPublic))
    }

    @Test
    fun testValidData() {
        assertTrue(liqPaySecurityImpl.validData(keyPrivate + data + keyPublic, signature))
    }

    @Test
    fun testValidData_False() {
        assertFalse(liqPaySecurityImpl.validData(data + keyPublic, signature))
    }
}