package com.example.venka.eventio.service.mail

import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.MessageStub

/**
 * Service for sending emails
 */
interface MailService {

    /**
     * Sends a message with events ids to a specific person
     *
     * @param to whom sends a message
     * @param messageStub message subject and template name
     * @param eventIds args for template
     */
    fun sendMessage(to: String, messageStub: MessageStub, eventIds: List<String> = emptyList())

    /**
     * Sends a message with event data to a specific person
     *
     * @param to whom send a message
     * @param messageStub message subject and template name
     * @param event event data for template
     */
    fun sendMessage(to: String, messageStub: MessageStub, event: Event)
}
