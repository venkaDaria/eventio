package com.example.venka.eventio.data.db.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.data.model.business.Place
import org.springframework.data.neo4j.annotation.Depth
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * Repository for {@link Place} entity
 */
@Monitor
interface PlaceRepository : Neo4jRepository<Place, String> {

    /**
     * Returns place by real address
     *
     * @param realAddress real address
     *
     * @return place or null
     */
    @Depth(2)
    fun findByRealAddress(realAddress: String): Place?

    /**
     * Returns place by room id
     *
     * @param roomId room id
     *
     * @return place or null
     */
    @Depth(2)
    fun findByRoomsId(roomId: String): Place?

    /**
     * Deletes all rooms and features that belong to a specific place
     *
     * @param id place id
     */
    @Query("MATCH (p:Place {id: {0}}) OPTIONAL MATCH (p)-[h:HAS]->(r:Room) " +
            "OPTIONAL MATCH (r)-[h2:HAS]->(f:Feature) DETACH DELETE p, r, f")
    fun deleteByIdWithChildren(id: String)

    /**
     * Returns owner of this room (place which has this room)
     *
     * @param roomId room id
     *
     * @return person or null
     */
    @Query("MATCH (l:LegalPerson)-[o:OWNS]->(p:Place)-[h:HAS]->(r:Room {id: {0}}) RETURN l, o, p, h, r")
    fun findOwner(roomId: String): Person?
}
