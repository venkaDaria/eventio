package com.example.venka.eventio.service.business.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.db.business.RoomRepository
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.AbstractService
import com.example.venka.eventio.service.business.RoomService
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Service

/**
 * Implementation of {@link RoomService}
 */
@Monitor
@Service
class RoomServiceImpl(neo4jRepository: Neo4jRepository<Room, String>)
    : AbstractService<Room, String, String>(neo4jRepository), RoomService {

    private val roomRepository = neo4jRepository as RoomRepository

    override fun deleteById(id: String) = roomRepository.deleteByIdWithChildren(id)
}
