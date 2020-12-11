package com.example.venka.eventio.data.model.business

import com.example.venka.eventio.data.model.Entity
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.id.UuidStrategy

/**
 * Represents entity Feature
 */
@NodeEntity
data class Feature(
        @Id
        @GeneratedValue(strategy = UuidStrategy::class)
        override var id: String? = null,

        @Property
        var name: String = "" // has projector etc.
) : Entity<String, String>(id) {
    override fun getParam(): String = name
}
