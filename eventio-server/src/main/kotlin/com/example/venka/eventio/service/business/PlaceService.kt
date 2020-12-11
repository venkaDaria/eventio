package com.example.venka.eventio.service.business

import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.service.Service

/**
 * Service for {@link Place}
 */
interface PlaceService : Service<Place, String> {

    /**
     * Returns place by room id
     *
     * @param roomId room id
     *
     * @return place or null
     */
    fun getByRoomId(roomId: String): Place?

    /**
     * Returns owner of this room (place which has this room)
     *
     * @param roomId room id
     *
     * @return person or null
     */
    fun getOwner(roomId: String): Person?
}
