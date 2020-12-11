package com.example.venka.eventio.data.model.business

import com.example.venka.eventio.data.model.Entity
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.id.UuidStrategy

/**
 * Represents entity Room
 */
@NodeEntity
data class Room(
        @Id
        @GeneratedValue(strategy = UuidStrategy::class)
        override var id: String? = null,

        @Property
        var name: String = "",

        @Property
        var price: String? = null,

        @Property
        var image: String? = null,

        @Property(name = "count")
        var countPeople: Int? = null,

        @Relationship(type = "HAS")
        var features: MutableSet<Feature> = HashSet()
) : Entity<String, String>(id) {
    override fun getParam(): String = name
}
