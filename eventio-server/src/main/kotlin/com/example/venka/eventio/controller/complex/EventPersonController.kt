package com.example.venka.eventio.controller.complex

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.person.NaturalPersonDto
import com.example.venka.eventio.data.dto.person.PersonDto
import com.example.venka.eventio.service.complex.impl.EventPersonService
import com.example.venka.eventio.translator.person.NaturalPersonTranslator
import com.example.venka.eventio.translator.person.PersonTranslator
import com.example.venka.eventio.utils.format.translateMap
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

/**
 * Controller for {@link Event} and {@link Person} entities
 */
@Monitor
@RestController
@RequestMapping("/event")
class EventPersonController(
        private val eventPersonService: EventPersonService,
        private val naturalPersonTranslator: NaturalPersonTranslator,
        private val personTranslator: PersonTranslator
) : Logging by LoggingImpl<EventPersonController>() {

    @GetMapping("/all-created")
    fun getEvents(principal: Principal): Map<String, Int> {
        log.debug("GET /event/all-created => get filtered events with ${principal.name}")

        return eventPersonService.findAllEvents(principal.name).translateMap()
    }

    @GetMapping("/subscribed")
    fun getSubscribed(principal: Principal): Map<String, Int> {
        log.debug("GET /event/subscribed => get filtered events with ${principal.name}")

        return eventPersonService.findAllSubscribedEvents(principal.name).translateMap()
    }

    @GetMapping("/created")
    fun getCreated(principal: Principal): Map<String, Int> {
        log.debug("GET /event/created => get filtered events with ${principal.name}")

        return eventPersonService.findAllCreatedEvents(principal.name).translateMap()
    }

    @GetMapping("/{id}/owner")
    fun getOwner(@PathVariable id: String): PersonDto {
        log.debug("GET /event/$id/owner => get owner of this event")

        return personTranslator.toDto(eventPersonService.getOwner(id))
    }

    @PostMapping("/{id}/unsubscribe")
    fun unsubscribe(@PathVariable id: String, principal: Principal): NaturalPersonDto? {
        log.debug("POST /event/$id/unsubscribe => ${principal.name} unsubscribed to this event")

        return naturalPersonTranslator.toDto(eventPersonService.unsubscribe(id, principal.name))
    }

    @PostMapping("/{id}/subscribe")
    fun subscribe(@PathVariable id: String, principal: Principal): NaturalPersonDto? {
        log.debug("POST /event/$id/subscribe => ${principal.name} subscribed to this event")

        return naturalPersonTranslator.toDto(eventPersonService.subscribe(id, principal.name))
    }
}
