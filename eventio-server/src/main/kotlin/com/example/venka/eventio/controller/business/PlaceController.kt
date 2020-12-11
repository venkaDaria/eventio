package com.example.venka.eventio.controller.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.MessageType
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.exception.NotUniqueException
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.business.PlaceService
import com.example.venka.eventio.service.business.RoomService
import com.example.venka.eventio.service.mail.MailService
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.translator.business.PlaceTranslator
import com.example.venka.eventio.utils.format.getStringOrNull
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
 * Controller for {@link Place} entity
 */
@Monitor
@RestController
@RequestMapping("/place")
class PlaceController(
        private val placeService: PlaceService,
        private val placeTranslator: PlaceTranslator,
        private val legalPersonService: LegalPersonService,
        private val eventService: EventService,
        private val roomService: RoomService,
        private val mailService: MailService
) : Logging by LoggingImpl<PlaceController>() {

    @GetMapping
    @Suppress("ReturnCount")
    fun getAll(@RequestParam locationFilter: Optional<Boolean>, authentication: Authentication): List<PlaceDto> {
        log.debug("GET /place => get all places")

        var list = placeService.getAll().map { placeTranslator.toDto(it) }

        locationFilter.ifPresent {
            if (!it) return@ifPresent

            log.debug("filter places with locationFilter=$locationFilter")

            val location = (authentication.principal as UserAuthenticationProvider.PersonDetails).location
                    ?: return@ifPresent

            list = list.filter { it.near(location) == true }
        }

        return list
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String, @RequestParam start: Optional<String>, @RequestParam end: Optional<String>)
            : PlaceDto? {
        val realEnd = end.getStringOrNull()
        val realStart = start.getStringOrNull()

        log.debug("GET /place/$id?start=$realStart&end=$realEnd => get place by id")

        val place = placeService.getById(id) ?: throw NotFoundException()

        val placeDto = placeTranslator.toDto(place)

        start.ifPresent {
            val predicate = { room: RoomDto -> eventService.isFree(room.id!!, realStart!!, realEnd) }

            val pair = placeDto.rooms.partition(predicate)
            with (placeDto) {
                rooms = pair.first.toMutableSet()
                bookedRooms = pair.second.toMutableSet()
            }
        }

        return placeDto
    }

    @GetMapping("/room/{roomId}")
    fun getByRoomId(@PathVariable roomId: String): PlaceDto? {
        log.debug("GET /place/room/$roomId => get place by room id")

        val place = placeService.getByRoomId(roomId) ?: throw NotFoundException()
        return placeTranslator.toDto(place)
    }

    @GetMapping("/")
    fun getByParam(@RequestParam param: String): PlaceDto? {
        log.debug("GET /place?param=$param => get places by realAddress")

        val place = placeService.getByParam(param) ?: throw NotFoundException()
        return placeTranslator.toDto(place)
    }

    @PostMapping
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun add(@RequestBody body: PlaceDto, authentication: Authentication): PlaceDto? {
        log.debug("POST /place => add place with body data")
        log.trace(body.toString())

        val place = placeService.getByParam(body.realAddress)
        whenNotNull(place) {
            throw NotUniqueException()
        }

        val newPlace = placeTranslator.fromDto(body)
        placeService.save(newPlace)

        // add place for a person
        val person = legalPersonService.getByParam(authentication.name) ?: throw NotFoundException()
        person.places.add(newPlace)
        legalPersonService.save(person)

        log.trace("Added place: $newPlace for person ${authentication.name}")

        return placeTranslator.toDto(newPlace)
    }

    @PutMapping
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun save(@RequestBody body: PlaceDto, authentication: Authentication)
            : PlaceDto? {
        log.debug("POST /place => add place with body data")
        log.trace(body.toString())

        val place = placeService.getByParam(body.realAddress) ?: throw NotFoundException()

        // transforms with rooms
        val newPlace = placeTranslator.fromDto(body, place.id)
        log.trace("New rooms: ${newPlace.rooms.joinToString()}")

        // this is empty only if rooms aren't changed, because every place has at least one room
        if (newPlace.rooms.isEmpty()) {
            log.trace("Load old rooms: ${place.rooms.joinToString()}")
            newPlace.rooms = place.rooms
        }

        placeService.save(newPlace)

        // find invalid rooms
        val diff = place.rooms.subtract(newPlace.rooms).map { it.id!! }
        diff.forEach {
            roomService.deleteById(it)
        }

        // find invalid events in this rooms
        val events = eventService.setInvalidLabelByRoomIds(diff)

        if (events.isNotEmpty()) {
            log.trace("Invalid events: ${events.joinToString()}")
            mailService.sendMessage(authentication.name, MessageType.INVALID_EVENT.simpleMessage, events)
        }

        log.trace("Added place: $newPlace")

        return placeTranslator.toDto(newPlace)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun remove(@PathVariable id: String, authentication: Authentication)
            : PlaceDto? {
        log.debug("DELETE /place/$id => delete place by id")

        val place = placeService.getById(id) ?: throw NotFoundException()

        val events = eventService.setInvalidLabelByPlace(id)
        placeService.deleteById(id)

        if (events.isNotEmpty()) {
            log.trace("Invalid events: ${events.joinToString()}")
            mailService.sendMessage(authentication.name, MessageType.INVALID_EVENT.simpleMessage, events)
        }

        log.trace("Deleted place: $place")

        return placeTranslator.toDto(place)
    }
}
