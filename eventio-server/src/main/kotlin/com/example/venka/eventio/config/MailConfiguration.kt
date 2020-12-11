package com.example.venka.eventio.config

import com.example.venka.eventio.data.model.MessageStub
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Mail templates configuration
 */
@Configuration
class MailConfiguration {

    @Bean
    fun invalidEventSimpleMessage(): MessageStub
            = MessageStub(templateName = "mail/mail-invalid-event")

    @Bean
    fun newEventForCreatorSimpleMessage(): MessageStub
            = MessageStub(templateName = "mail/mail-new-event-creator")

    @Bean
    fun newEventForCompanySimpleMessage(): MessageStub
            = MessageStub(templateName = "mail/mail-new-event-company")

    @Bean
    fun updatedEventForSubscribersSimpleMessage(): MessageStub
            = MessageStub(templateName = "mail/mail-updated-event-subscribers")

    @Bean
    fun updatedEventForCompanySimpleMessage(): MessageStub
            = MessageStub(templateName = "mail/mail-updated-event-company")
}
