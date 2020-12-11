package com.example.venka.eventio.data.model

import com.example.venka.eventio.data.model.business.Place
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.id.UuidStrategy

/**
 * Represents entity Person
 *
 * His descendants can be only NaturalPerson or LegalPerson (sealed)
 */
@NodeEntity
sealed class Person(
        @Id
        @GeneratedValue(strategy = UuidStrategy::class)
        override var id: String? = null,

        @Property
        open var email: String = "",

        @Relationship(type = "CREATES")
        open var createdEvents: MutableSet<Event> = HashSet()
) : Entity<String, String>(id) {
    override fun getParam(): String = email
}

/**
 * Represents entity NaturalPerson
 */
@NodeEntity
data class NaturalPerson(
        @Id
        @GeneratedValue(strategy = UuidStrategy::class)
        override var id: String? = null,

        override var email: String = "",

        @Property
        var location: String? = null,

        @Property
        var phone: String? = null,

        @Relationship(type = "SUBSCRIBES")
        var subscribedEvents: MutableSet<Event> = HashSet(),

        override var createdEvents: MutableSet<Event> = HashSet()
) : Person(id, email, createdEvents)

private const val COMMON_PATH = "company-"

/**
 * Represents entity LegalPerson
 */
@NodeEntity
data class LegalPerson(
        @Id
        @GeneratedValue(strategy = UuidStrategy::class)
        override var id: String? = null,

        override var email: String = "",

        @Property
        private var _name: String = "",

        @Property
        var info: String? = null,

        @Relationship(type = "OWNS")
        var places: MutableSet<Place> = HashSet(),

        @Relationship(type = "CREATES")
        override var createdEvents: MutableSet<Event> = HashSet()
) : Person(id, email, createdEvents) {

    /**
     * Overrides setter for name
     */
    var name: String = _name
        set(value) {
            field = value
            url = createUrl(value)
        }

    @Property(name = "url")
    var url: String = createUrl(name)

    // url is produced by name
    private fun createUrl(value: String) = COMMON_PATH + value.toLowerCase()
}
