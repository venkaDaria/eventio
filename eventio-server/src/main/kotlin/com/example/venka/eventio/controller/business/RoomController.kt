package com.example.venka.eventio.controller.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.business.FeatureDto
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.service.business.RoomService
import com.example.venka.eventio.translator.business.FeatureTranslator
import com.example.venka.eventio.translator.business.RoomTranslator
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for {@link Room} entity
 */
@Monitor
@RestController
@RequestMapping("/room")
class RoomController(
        private val roomService: RoomService,
        private val roomTranslator: RoomTranslator,
        private val featureTranslator: FeatureTranslator
) : Logging by LoggingImpl<RoomController>() {

    @GetMapping
    fun getAll(): List<RoomDto> {
        log.debug("GET /room => get all rooms")

        return roomService.getAll().map { roomTranslator.toDto(it) }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): RoomDto? {
        log.debug("GET /room/$id => get room by id")

        val room = roomService.getById(id) ?: throw NotFoundException()
        return roomTranslator.toDto(room)
    }

    @PostMapping
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun add(@RequestBody body: RoomDto): RoomDto? {
        log.debug("POST /room => add room with body data")
        log.trace(body.toString())

        val newRoom = roomTranslator.fromDto(body)
        roomService.save(newRoom)

        log.trace("Added room: $newRoom")

        return roomTranslator.toDto(newRoom)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun addFeature(@PathVariable id: String, @RequestBody body: FeatureDto): FeatureDto {
        log.debug("PUT /room => add feature to $id room with body data")
        log.trace(body.toString())

        val room = roomService.getById(id) ?: throw NotFoundException()

        val newFeature = featureTranslator.fromDto(body)
        room.features.add(newFeature)

        roomService.save(room)

        log.trace("Added feature: $newFeature")

        return featureTranslator.toDto(newFeature)
    }

    @PutMapping
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun save(@RequestBody body: RoomDto): RoomDto? {
        log.debug("PUT /room => add room with body data")
        log.trace(body.toString())

        val room = roomService.getById(body.id!!) ?: throw NotFoundException()

        val newRoom = roomTranslator.fromDto(body, room.id)
        newRoom.features = room.features
        roomService.save(newRoom)

        log.trace("Added room: $newRoom")

        return roomTranslator.toDto(newRoom)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LEGAL_PERSON')")
    fun remove(@PathVariable id: String): RoomDto? {
        log.debug("DELETE /room/$id => delete room by id")

        val room = roomService.getById(id) ?: throw NotFoundException()
        roomService.deleteById(id)

        log.trace("Deleted room: $room")

        return roomTranslator.toDto(room)
    }
}
