package com.example.venka.eventio.data.model

import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

const val SUBJECT = "EVENTIO: issue with your events"

/**
 * Main message information
 */
data class MessageStub(val subject: String = SUBJECT, val templateName: String)

/**
 * Messages for sending information mail
 */
enum class MessageType {

    NEW_EVENT_FOR_CREATOR,
    NEW_EVENT_FOR_COMPANY,
    UPDATED_EVENT_FOR_SUBSCRIBERS,
    UPDATED_EVENT_FOR_COMPANY,
    INVALID_EVENT;

    lateinit var simpleMessage: MessageStub

    @Component
    class MessageTypeServiceInjector(
        private val invalidEventSimpleMessage: MessageStub,
        private val newEventForCreatorSimpleMessage: MessageStub,
        private val newEventForCompanySimpleMessage: MessageStub,
        private val updatedEventForSubscribersSimpleMessage: MessageStub,
        private val updatedEventForCompanySimpleMessage: MessageStub
    ) {
        @PostConstruct
        fun postConstruct() {
            MessageType.INVALID_EVENT.simpleMessage = invalidEventSimpleMessage
            MessageType.NEW_EVENT_FOR_CREATOR.simpleMessage = newEventForCreatorSimpleMessage
            MessageType.NEW_EVENT_FOR_COMPANY.simpleMessage = newEventForCompanySimpleMessage
            MessageType.UPDATED_EVENT_FOR_SUBSCRIBERS.simpleMessage = updatedEventForSubscribersSimpleMessage
            MessageType.UPDATED_EVENT_FOR_COMPANY.simpleMessage = updatedEventForCompanySimpleMessage
        }
    }
}
