package com.example.venka.eventio.service.mail

import com.example.venka.eventio.config.security.WEB_URL
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.MessageStub
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine

/**
 * Implementation of {@link MailService}
 */
@Component
class MailServiceImpl(
        val emailSender: JavaMailSender,
        val templateEngine: SpringTemplateEngine
) : MailService, Logging by LoggingImpl<MailServiceImpl>() {

    /**
     * Sends a simple mime message with {@link JavaMailSender}
     *
     * @param to whom sends a message
     * @param subject subject of this message
     * @param text text of this message
     */
    fun sendSimpleMessage(to: String, subject: String, text: String) {
        val message = emailSender.createMimeMessage()

        with(MimeMessageHelper(message, true)) {
            setTo(to)
            setSubject(subject)
            setText(text, true)
        }

        emailSender.send(message)
    }

    override fun sendMessage(to: String, messageStub: MessageStub, eventIds: List<String>) =
        sendMessage(to, messageStub) {
            it.setVariable("ids", eventIds.joinToString())
        }

    override fun sendMessage(to: String, messageStub: MessageStub, event: Event) =
        sendMessage(to, messageStub) {
            it.setVariable("link", event.toLink())
        }

    private fun sendMessage(to: String, messageStub: MessageStub, setVars: (Context) -> Unit) {
        val text = handleTemplate(messageStub.templateName, setVars)

        sendSimpleMessage(to, messageStub.subject, text)
    }

    private fun handleTemplate(template: String, setVars: (Context) -> Unit): String =
        with (Context()) {
            setVars(this)
            return templateEngine.process(template, this)
        }

    private fun Event.toLink(): String = "$WEB_URL/event/${this.id}"
}
