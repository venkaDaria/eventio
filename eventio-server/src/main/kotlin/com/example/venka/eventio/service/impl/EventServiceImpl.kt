package com.example.venka.eventio.service.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.db.EventRepository
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.service.AbstractService
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.utils.format.parse
import com.example.venka.eventio.utils.notTheSameTime
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Service

/**
 * Implementation of {@link EventService}
 */
@Monitor
@Service
class EventServiceImpl(neo4jRepository: Neo4jRepository<Event, String>)
    : AbstractService<Event, String, String>(neo4jRepository), EventService {

    private val eventRepository = neo4jRepository as EventRepository

    override fun deleteAllInvalid(): List<String> = eventRepository.deleteAllInvalid()

    override fun getByParam(param: String): Event? = eventRepository.findByTitle(param)

    override fun getOwner(id: String): Person = eventRepository.findOwner(id)

    override fun setInvalidLabelByPerson(email: String) = eventRepository.setInvalidLabelByPerson(email)

    override fun setInvalidLabelByPlace(id: String) = eventRepository.setInvalidLabelByPlace(id)

    override fun setInvalidLabelByRoomIds(ids: List<String>) = ids.flatMap {
        eventRepository.setInvalidLabelByRoomId(it)
    }

    override fun getSubscribers(id: String): List<String> = eventRepository.findSubscribers(id)

    override fun isFree(roomId: String, start: String, end: String?): Boolean {
        val events = eventRepository.findByLocation(roomId)

        val startDateTime = start.parse()
        val endDateTime = end?.parse()

        return events.all {
            it.notTheSameTime(startDateTime, endDateTime)
        }
    }
}
