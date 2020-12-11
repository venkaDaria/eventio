package com.example.venka.eventio.data.dto.business

/**
 * Dto for {@link Room}
 */
data class RoomDto(
        var id: String? = null,
        var name: String = "",
        var price: String? = null,
        var image: String? = null,
        var count: Int? = null,
        var features: Set<FeatureDto> = HashSet()
)
