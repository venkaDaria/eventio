package com.example.venka.eventio.service.complex.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.business.PlaceService
import com.example.venka.eventio.service.business.RoomService
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.utils.format.parse
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import com.example.venka.eventio.utils.notTheSameTime
import org.springframework.stereotype.Service
import java.security.Principal
import java.util.Optional

/**
 * Service for complex operations between business and events
 */
@Monitor
@Service
class EventBusinessService(
        private val legalPersonService: LegalPersonService,
        private val roomService: RoomService,
        private val placeService: PlaceService,
        private val eventService: EventService
) : Logging by LoggingImpl<EventBusinessService>() {

    /**
     * Find all events located in specific place
     *
     * @param placeId id of a place
     *
     * @return list of events
     */
    fun findAllByPlaceId(placeId: String): List<Event> {
        val place = placeService.getById(placeId) ?: return emptyList()

        return eventService.getAll().filter {
            place.rooms.any { room -> it.location == room }
        }
    }

    /**
     * Find all events located in specific room
     *
     * @param roomId id of a room
     *
     * @return list of events
     */
    fun findAllByRoomId(roomId: String): List<Event> {
        val room = roomService.getById(roomId) ?: return emptyList()

        return eventService.getAll().filter {
            it.location == room
        }
    }

    /**
     * Find all places
     *
     * @param start date of start
     * @param end date of end
     *
     * @return list of places
     */
    fun findAllBetweenDates(principal: Principal, start: String, end: Optional<String>, onlyMine: Boolean)
            : List<Place> {
        val startDateTime = start.parse()
        val endDateTime = end.map { it.parse() }

        val events = eventService.getAll()
        val places = placeService.getAll()

        val placeEvents = events.groupBy {
            it.location
        }

        return placeEvents.filter {
            it.value.all {
                it.notTheSameTime(startDateTime, endDateTime.orElse(null))
            }
        }.mapNotNull {
            places.singleOrNull { place -> place.rooms.any { room -> room == it.key } }
        }.distinct().filter {
            !onlyMine || isMine(it, principal as UserAuthenticationProvider.PersonDetails)
        }
    }

    private fun isMine(place: Place, principal: UserAuthenticationProvider.PersonDetails): Boolean {
        val places = legalPersonService.getByParam(principal.email)!!.places
        return places.any { it.realAddress == place.realAddress }
    }
}
