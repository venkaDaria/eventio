package com.example.venka.eventio.data.model

import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.utils.format.startDateTime
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.id.UuidStrategy
import java.time.LocalDateTime

/**
 * Represents entity Event
 */
@NodeEntity
data class Event(
        @Id
        @GeneratedValue(strategy = UuidStrategy::class)
        override var id: String? = null,

        @Property
        var title: String = "",

        @Property
        var description: String? = null,

        @Property
        var image: String? = null,

        @Property
        var start: LocalDateTime = startDateTime(),

        @Property
        var end: LocalDateTime? = null,

        @Property
        var mode: Mode = Mode.LINK,

        @Relationship(type = "TAKES_PLACE")
        var location: Room = Room()
) : Entity<String, String>(id) {
    override fun getParam(): String = title
}
