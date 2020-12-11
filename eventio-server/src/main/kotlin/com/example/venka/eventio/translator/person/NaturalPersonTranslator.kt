package com.example.venka.eventio.translator.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.person.NaturalPersonDto
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.Translator
import org.springframework.stereotype.Service

/**
 * NaturalPerson Translator
 */
@Monitor
@Service
class NaturalPersonTranslator(private val eventTranslator: EventTranslator)
    : Translator<NaturalPerson, NaturalPersonDto, String> {

    override fun fromDto(source: NaturalPersonDto, id: String?): NaturalPerson {
        return NaturalPerson(
                id,
                source.email,
                source.location,
                source.phone
        )
    }

    override fun toDto(source: NaturalPerson): NaturalPersonDto {
        return NaturalPersonDto(
                source.email,
                source.location,
                source.phone,
                source.subscribedEvents.map { eventTranslator.toDto(it) }.toMutableSet(),
                source.createdEvents.map { eventTranslator.toDto(it) }.toMutableSet()
        )
    }
}
