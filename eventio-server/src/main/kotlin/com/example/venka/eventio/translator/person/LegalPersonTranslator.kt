package com.example.venka.eventio.translator.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.person.LegalPersonDto
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.Translator
import com.example.venka.eventio.translator.business.PlaceTranslator
import org.springframework.stereotype.Service

/**
 * LegalPerson Translator
 */
@Monitor
@Service
class LegalPersonTranslator(
        private val eventTranslator: EventTranslator,
        private val placeTranslator: PlaceTranslator
) : Translator<LegalPerson, LegalPersonDto, String> {

    override fun fromDto(source: LegalPersonDto, id: String?): LegalPerson {
        return LegalPerson(
                id,
                source.email,
                source.name,
                source.info
        )
    }

    override fun toDto(source: LegalPerson): LegalPersonDto {
        return LegalPersonDto(
                source.email,
                source.name,
                source.url,
                source.info,
                source.places.map { placeTranslator.toDto(it) }.toMutableSet(),
                source.createdEvents.map { eventTranslator.toDto(it) }.toMutableSet()
        )
    }
}
