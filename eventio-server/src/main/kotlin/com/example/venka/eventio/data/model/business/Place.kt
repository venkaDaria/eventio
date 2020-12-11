package com.example.venka.eventio.data.model.business

import com.example.venka.eventio.data.model.Entity
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.id.UuidStrategy
import java.util.HashSet
import javax.validation.constraints.Pattern

/**
 * Represents entity Place
 */
@NodeEntity
data class Place(
        @Id
        @GeneratedValue(strategy = UuidStrategy::class)
        override var id: String? = null,

        @Property
        var name: String = "",

        @Property(name = "address")
        var realAddress: String = "",

        @Property
        @Pattern(regexp = "\\d{2}:\\d{2}-\\d{2}:\\d{2}")
        var timeWork: String? = null,

        @Property(name = "image")
        var image: String? = null,

        @Relationship(type = "HAS")
        var rooms: MutableSet<Room> = HashSet()
) : Entity<String, String>(id) {
    override fun getParam(): String = realAddress
}
