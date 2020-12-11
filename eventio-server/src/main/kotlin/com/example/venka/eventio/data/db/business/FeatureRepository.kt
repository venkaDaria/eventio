package com.example.venka.eventio.data.db.business

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.model.business.Feature
import org.springframework.data.neo4j.repository.Neo4jRepository

/**
 * Repository for {@link Feature} entity
 */
@Monitor
interface FeatureRepository : Neo4jRepository<Feature, String>
