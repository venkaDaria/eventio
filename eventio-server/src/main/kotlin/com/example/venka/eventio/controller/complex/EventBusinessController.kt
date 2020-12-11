package com.example.venka.eventio.controller.complex

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.service.complex.impl.EventBusinessService
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.business.PlaceTranslator
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.Optional

/**
 * Controller for {@link Event} and {@link Person} entities
 */
@Monitor
@RestController
@RequestMapping("/event")
class EventBusinessController(
        private val eventBusinessService: EventBusinessService,
        private val eventTranslator: EventTranslator,
        private val placeTranslator: PlaceTranslator
) : Logging by LoggingImpl<EventBusinessController>() {

    @GetMapping("/places/{placeId}")
    fun getAllByPlaceId(@PathVariable placeId: String): List<EventDto> {
        log.debug("GET /event/places/$placeId => get all events by place id")

        return eventBusinessService.findAllByPlaceId(placeId).map {
            eventTranslator.toDto(it)
        }
    }

    @GetMapping("/rooms/{roomId}")
    fun getAllByRoomId(@PathVariable roomId: String): List<EventDto> {
        log.debug("GET /event/rooms/$roomId => get all events by room id")

        return eventBusinessService.findAllByRoomId(roomId).map {
            eventTranslator.toDto(it)
        }
    }

    @GetMapping("/places")
    fun getAllBetweenDates(principal: Principal, @RequestParam start: String, @RequestParam end: Optional<String>,
                           @RequestParam onlyMine: Boolean): List<PlaceDto> {
        log.debug("GET /event/places => get all events between two dates")

        return eventBusinessService.findAllBetweenDates(principal, start, end, onlyMine).map {
            placeTranslator.toDto(it)
        }
    }
}
