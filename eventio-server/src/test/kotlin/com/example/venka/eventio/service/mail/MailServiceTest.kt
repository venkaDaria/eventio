package com.example.venka.eventio.service.mail

import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.MessageType
import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetupTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

const val TO_VALUE = "hello"

@SpringBootTest
class MailServiceTest : AbstractTestNGSpringContextTests() {

    @Autowired
    private lateinit var mailService: MailService

    private lateinit var greenMail: GreenMail

    @BeforeMethod
    fun startMailServer() {
        greenMail = GreenMail(ServerSetupTest.SMTP)
        greenMail.start()
    }

    @AfterMethod
    fun stopMailServer() {
        greenMail.stop()
    }

    @Test
    fun sendMessage() {
        mailService.sendMessage(TO_VALUE, MessageType.INVALID_EVENT.simpleMessage, listOf("hello, hello2"))

        assertEquals(greenMail.receivedMessages.size, 1)
    }

    @Test
    fun sendMessage_WithEvent() {
        mailService.sendMessage(TO_VALUE, MessageType.NEW_EVENT_FOR_CREATOR.simpleMessage, Event(title="hello"))

        assertEquals(greenMail.receivedMessages.size, 1)
    }
}
