package com.example.venka.eventio.data.dto.person

import com.example.venka.eventio.data.dto.EventDto

/**
 * Dto for {@link NaturalPerson}
 */
data class NaturalPersonDto(
        var email: String = "",
        var location: String? = null,
        var phone: String? = null,
        var subscribedEvents: MutableSet<EventDto> = HashSet(),
        var createdEvents: MutableSet<EventDto> = HashSet()
)
