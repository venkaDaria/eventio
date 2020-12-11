package com.example.venka.eventio.service.business.impl

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.service.AbstractService
import com.example.venka.eventio.service.business.FeatureService
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Service

/**
 * Implementation of {@link FeatureService}
 */
@Monitor
@Service
class FeatureServiceImpl(neo4jRepository: Neo4jRepository<Feature, String>)
    : AbstractService<Feature, String, String>(neo4jRepository), FeatureService
