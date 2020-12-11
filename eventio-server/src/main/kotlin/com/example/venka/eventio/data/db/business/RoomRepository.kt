package com.example.venka.eventio.data.db.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.business.Room
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * Repository for {@link Room} entity
 */
@Monitor
interface RoomRepository : Neo4jRepository<Room, String> {

    /**
     * Deletes all features that belong to a specific place
     *
     * @param id room id
     */
    @Query("MATCH (r:Room {id: {0}}) OPTIONAL MATCH (r)-[h:HAS]->(f:Feature) DETACH DELETE r, f")
    fun deleteByIdWithChildren(id: String)
}
