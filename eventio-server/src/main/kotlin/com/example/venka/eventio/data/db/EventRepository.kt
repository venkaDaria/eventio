package com.example.venka.eventio.data.db

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.Person
import org.springframework.data.neo4j.annotation.Depth
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * Repository for {@link Event} entity
 */
@Monitor
interface EventRepository : Neo4jRepository<Event, String> {

    /**
     * Returns event by title
     *
     * @param title event's title
     *
     * @return event or null
     */
    @Depth(2)
    fun findByTitle(title: String): Event?

    /**
     * Returns all events in a specific location
     *
     * @param roomId room id
     *
     * @return list of events
     */
    @Depth(2)
    fun findByLocation(roomId: String): List<Event>

    /**
     * Returns who created an event by event id
     *
     * @param id event id
     *
     * @return person created this event
     */
    @Query("MATCH (p:Person)-[r:CREATES]->(e:Event {id: {0}}) RETURN p")
    fun findOwner(id: String): Person

    /**
     * Deletes all invalid events
     *
     * @return ids of deleted events
     */
    @Query("MATCH (e:Invalid) WITH e, e.id AS id DETACH DELETE e RETURN id")
    fun deleteAllInvalid(): List<String>

    /**
     * Sets invalid label for a specific event
     *
     * @param id event id
     *
     * @return event id
     */
    @Query("MATCH (e:Event {id: {0}}) SET e:Invalid RETURN e.id")
    fun setInvalidLabel(id: String): String

    /**
     * Sets invalid label for events by person who created them
     *
     * @param email person email
     *
     * @return ids of invalid events
     */
    @Query("MATCH (p:Person {email: {0}})-[r:CREATES]->(e:Event) SET e:Invalid RETURN e.id")
    fun setInvalidLabelByPerson(email: String): List<String>

    /**
     * Sets invalid label for events by place where they located
     *
     * @param id place id
     *
     * @return ids of invalid events
     */
    @Query("MATCH (p:Place {id: {0}})-[h:HAS]->(r:Room)<-[t:TAKES_PLACE]-(e:Event) SET e:Invalid RETURN e.id")
    fun setInvalidLabelByPlace(id: String): List<String>

    /**
     * Sets invalid label for events by rooms where they located
     *
     * @param ids rooms' ids
     *
     * @return ids of invalid events
     */
    @Query("MATCH (r:Room {id: {0}})<-[t:TAKES_PLACE]-(e:Event) SET e:Invalid RETURN e.id")
    fun setInvalidLabelByRoomId(id: String): List<String>

    /**
     * Returns subscribers by event id
     *
     * @param id event id
     *
     * @return list of persons' ids
     */
    @Query("MATCH (p:NaturalPerson)-[s:SUBSCRIBES]->(e:Event {id: {0}}) RETURN p.email")
    fun findSubscribers(id: String): List<String>
}
