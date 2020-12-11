package com.example.venka.eventio.controller

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.model.MessageType
import com.example.venka.eventio.data.model.Mode
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.exception.NotUniqueException
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.business.PlaceService
import com.example.venka.eventio.service.complex.impl.EventPersonService
import com.example.venka.eventio.service.mail.MailService
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import com.example.venka.eventio.utils.near
import com.example.venka.eventio.utils.whenNotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

/**
 * Controller for {@link Event} entity
 */
@Monitor
@RestController
@RequestMapping("/event")
class EventController(
        private val eventService: EventService,
        private val placeService: PlaceService,
        private val eventTranslator: EventTranslator,
        private val mailService: MailService,
        private val eventPersonService: EventPersonService
) : Logging by LoggingImpl<EventController>() {

    @GetMapping
    @PreAuthorize("#isPublic.orElse(false) == true")
    @Suppress("ReturnCount")
    fun getAll(
            @RequestParam isPublic: Optional<Boolean>,
            @RequestParam locationFilter: Optional<Boolean>,
            authentication: Authentication?
    ): List<EventDto> {
        log.debug("GET /event => get all events")

        var list = eventService.getAll().map { eventTranslator.toDto(it) }

        isPublic.ifPresent {
            if (!it) return@ifPresent

            log.debug("filter events with mode=$isPublic")
            list = list.filter { el -> el.mode === Mode.PUBLIC }
        }

       locationFilter.ifPresent {
            if (!it) return@ifPresent

            log.debug("filter events with locationFilter=$locationFilter")

            val location = (authentication?.principal as UserAuthenticationProvider.PersonDetails).location
                    ?: return@ifPresent
            val places = placeService.getAll()

            list = list.filter { event ->
                val place = places.singleOrNull { it.rooms.any { it.id == event.location.id } }
                place?.near(location) == true
            }
        }

        return list
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String, authentication: Authentication): EventDto? {
        log.debug("GET /event/$id => get event by id")

        val event = eventService.getById(id) ?: throw NotFoundException()
        val owner = eventService.getOwner(id)

        if (event.mode === Mode.PRIVATE && authentication.name !== owner.email) {
            throw NotFoundException()
        }

        return eventTranslator.toDto(event)
    }

    @GetMapping("/")
    fun getByParam(@RequestParam param: String): EventDto? {
        log.debug("GET /event?param=$param => get events by title")

        val event = eventService.getByParam(param) ?: throw NotFoundException()
        return eventTranslator.toDto(event)
    }

    @PostMapping
    fun add(@RequestBody body: EventDto, authentication: Authentication): EventDto? {
        log.debug("POST /event => add event with body data")
        log.trace(body.toString())

        val event = eventService.getByParam(body.title)
        whenNotNull(event) {
            throw NotUniqueException()
        }

        val newEvent = eventTranslator.fromDto(body)
        val savedEvent = eventService.save(newEvent)

        log.trace("Added event: $newEvent")

        eventPersonService.create(savedEvent, authentication.name)

        mailService.sendMessage(authentication.name, MessageType.NEW_EVENT_FOR_CREATOR.simpleMessage, newEvent)

        val owner = placeService.getOwner(savedEvent.location.id!!)
        log.trace("Owner: $owner")
        mailService.sendMessage(owner!!.email, MessageType.NEW_EVENT_FOR_COMPANY.simpleMessage, newEvent)

        return eventTranslator.toDto(newEvent)
    }

    @PutMapping
    fun save(@RequestBody body: EventDto): EventDto? {
        log.debug("PUT /event => save event with body data")
        log.trace(body.toString())

        val event = eventService.getByParam(body.title) ?: throw NotFoundException()

        val newEvent = eventTranslator.fromDto(body, event.id)
        val savedEvent = eventService.save(newEvent)

        log.trace("Added event: $newEvent")

        val subscribers = eventService.getSubscribers(savedEvent.id!!)
        subscribers.forEach {
            mailService.sendMessage(it, MessageType.UPDATED_EVENT_FOR_SUBSCRIBERS.simpleMessage, newEvent)
        }

        val owner = placeService.getOwner(savedEvent.location.id!!)
        log.trace("Owner: $owner")
        mailService.sendMessage(owner!!.email, MessageType.UPDATED_EVENT_FOR_COMPANY.simpleMessage, newEvent)

        return eventTranslator.toDto(newEvent)
    }

    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: String): EventDto? {
        log.debug("DELETE /event/$id => delete event by id")

        val event = eventService.getById(id) ?: throw NotFoundException()
        eventService.deleteById(id)

        log.trace("Deleted event: $event")

        return eventTranslator.toDto(event)
    }

    @DeleteMapping("/invalid")
    fun removeAllInvalid(): List<String> {
        log.debug("DELETE /event/invalid => delete all invalid events")

        val events = eventService.deleteAllInvalid()

        log.trace("Deleted events: ${events.joinToString()}")

        return events
    }
}
