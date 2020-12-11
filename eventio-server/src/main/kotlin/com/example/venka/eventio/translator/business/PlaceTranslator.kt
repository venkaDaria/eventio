package com.example.venka.eventio.translator.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.translator.Translator
import org.springframework.stereotype.Service

/**
 * Place Translator
 */
@Monitor
@Service
class PlaceTranslator(private val roomTranslator: RoomTranslator) : Translator<Place, PlaceDto, String> {
    override fun fromDto(source: PlaceDto, id: String?): Place = Place(
            id ?: source.id,
            source.name,
            source.realAddress,
            source.timeWork,
            source.image,
            source.rooms.map { roomTranslator.fromDto(it) }.toMutableSet()
    )

    override fun toDto(source: Place): PlaceDto = PlaceDto(
            source.id,
            source.name,
            source.realAddress,
            source.timeWork,
            source.image,
            source.rooms.map { roomTranslator.toDto(it) }.toMutableSet()
    )
}
