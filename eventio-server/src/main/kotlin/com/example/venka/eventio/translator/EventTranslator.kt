package com.example.venka.eventio.translator

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.translator.business.RoomTranslator
import com.example.venka.eventio.utils.format.formatToString
import com.example.venka.eventio.utils.format.parse
import org.springframework.stereotype.Service

/**
 * Event Translator
 */
@Monitor
@Service
class EventTranslator(private val roomTranslator: RoomTranslator) : Translator<Event, EventDto, String> {
    override fun fromDto(source: EventDto, id: String?): Event {
        return Event(
                id ?: source.id,
                source.title,
                source.description,
                source.image,
                source.start.parse(),
                source.end?.parse(),
                source.mode,
                roomTranslator.fromDto(source.location)
        )
    }

    override fun toDto(source: Event): EventDto {
        return EventDto(
                source.id,
                source.title,
                source.description,
                source.image,
                source.start.formatToString(),
                source.end?.formatToString(),
                source.mode,
                roomTranslator.toDto(source.location)
        )
    }
}
