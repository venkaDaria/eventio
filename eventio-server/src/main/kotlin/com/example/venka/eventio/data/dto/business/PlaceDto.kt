package com.example.venka.eventio.data.dto.business

/**
 * Dto for {@link Place}
 */
data class PlaceDto(
        var id: String? = null,
        var name: String = "",
        var realAddress: String = "",
        var timeWork: String? = null,
        var image: String? = null,
        var rooms: MutableSet<RoomDto> = HashSet(),
        var bookedRooms: Set<RoomDto> = HashSet()
)
