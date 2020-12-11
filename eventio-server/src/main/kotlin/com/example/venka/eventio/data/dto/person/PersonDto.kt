package com.example.venka.eventio.data.dto.person

import com.example.venka.eventio.data.dto.EventDto

/**
 * Dto for {@link Person}
 */
data class PersonDto(
        var email: String = "",
        var createdEvents: MutableSet<EventDto> = HashSet(),
        var subscribedEvents: MutableSet<EventDto> = HashSet()
)
