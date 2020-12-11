package com.example.venka.eventio.data.dto

import com.example.venka.eventio.data.dto.business.RoomDto
import com.example.venka.eventio.data.model.Mode
import com.example.venka.eventio.utils.format.formatToString
import com.example.venka.eventio.utils.format.startDateTime

/**
 * Dto for {@link Event}
 */
data class EventDto(
        var id: String? = null,
        var title: String = "",
        var description: String? = null,
        var image: String? = null,
        var start: String = startDateTime().formatToString(),
        var end: String? = null,
        var mode: Mode = Mode.LINK,
        var location: RoomDto
)
