package com.example.venka.eventio.data.dto.person

import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.business.PlaceDto

/**
 * Dto for {@link LegalPerson}
 */
data class LegalPersonDto(
        var email: String = "",
        var name: String = "",
        var url: String = "",
        var info: String? = null,
        var places: MutableSet<PlaceDto> = HashSet(),
        var createdEvents: MutableSet<EventDto> = HashSet()
)
