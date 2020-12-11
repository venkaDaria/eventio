package com.example.venka.eventio.service

import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.Person

/**
 * Service for {@link Event}
 */
interface EventService : Service<Event, String> {

    /**
     * Returns who created an event by event id
     *
     * @param id event id
     *
     * @return person created this event
     */
    fun getOwner(id: String): Person

    /**
     * Deletes all invalid events
     *
     * @return ids of deleted events
     */
    fun deleteAllInvalid(): List<String>

    /**
     * Sets invalid label for events by person who created them
     *
     * @param email person email
     *
     * @return ids of invalid events
     */
    fun setInvalidLabelByPerson(email: String): List<String>

    /**
     * Sets invalid label for events by place where they located
     *
     * @param id place id
     *
     * @return ids of invalid events
     */
    fun setInvalidLabelByPlace(id: String): List<String>

    /**
     * Sets invalid label for events by rooms where they located
     *
     * @param ids rooms' ids
     *
     * @return ids of invalid events
     */
    fun setInvalidLabelByRoomIds(ids: List<String>): List<String>

    /**
     * Returns subscribers by event id
     *
     * @param id event id
     *
     * @return list of persons' ids
     */
    fun getSubscribers(id: String): List<String>

    /**
     * Returns true if no events are booked in this time in this room, otherwise - false
     *
     * @param roomId room id
     * @param start start of a possible event
     * @param end end of a possible event
     *
     * @return boolean - room is free or not
     */
    fun isFree(roomId: String, start: String, end: String?): Boolean
}
