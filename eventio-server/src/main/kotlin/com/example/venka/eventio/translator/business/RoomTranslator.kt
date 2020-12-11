package com.example.venka.eventio.translator.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.translator.Translator
import org.springframework.stereotype.Service

/**
 * Room Translator
 */
@Monitor
@Service
class RoomTranslator(private val featureTranslator: FeatureTranslator) : Translator<Room, RoomDto, String> {
    override fun fromDto(source: RoomDto, id: String?): Room {
        return Room(
                id ?: source.id,
                source.name,
                source.price,
                source.image,
                source.count
        )
    }

    override fun toDto(source: Room): RoomDto {
        return RoomDto(
                source.id,
                source.name,
                source.price,
                source.image,
                source.countPeople,
                source.features.map { featureTranslator.toDto(it) }.toMutableSet()
        )
    }
}
