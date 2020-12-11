package com.example.venka.eventio.service.business.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.db.business.PlaceRepository
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.service.AbstractService
import com.example.venka.eventio.service.business.PlaceService
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Service

/**
 * Implementation of {@link PlaceService}
 */
@Monitor
@Service
class PlaceServiceImpl(neo4jRepository: Neo4jRepository<Place, String>)
    : AbstractService<Place, String, String>(neo4jRepository), PlaceService {
    private val placeRepository = neo4jRepository as PlaceRepository

    override fun getByParam(param: String): Place? = placeRepository.findByRealAddress(param)

    override fun getByRoomId(roomId: String): Place? = placeRepository.findByRoomsId(roomId)

    override fun deleteById(id: String) = placeRepository.deleteByIdWithChildren(id)

    override fun getOwner(roomId: String): Person? = placeRepository.findOwner(roomId)
}
