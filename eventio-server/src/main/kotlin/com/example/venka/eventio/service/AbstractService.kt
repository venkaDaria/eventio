package com.example.venka.eventio.service

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.Entity
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.data.neo4j.repository.Neo4jRepository
import java.io.Serializable

/**
 * Simple implementation of {@link Service} interface
 */
@Monitor
abstract class AbstractService<T : Entity<R, K>, R : Serializable, out K>(
        private val neo4jRepository: Neo4jRepository<T, R>
) : Service<T, R>, Logging by LoggingImpl<AbstractService<T, R, K>>() {

    override fun getAll(): List<T> = neo4jRepository.findAll(2).toList()

    override fun save(entity: T): T = neo4jRepository.save(entity, 2)

    override fun deleteById(id: R) = neo4jRepository.deleteById(id)

    override fun getById(id: R): T? = neo4jRepository.findById(id, 2).orElse(null)

    override fun deleteAll() = neo4jRepository.deleteAll()
}
