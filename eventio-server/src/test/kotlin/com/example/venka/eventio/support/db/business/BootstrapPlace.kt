package com.example.venka.eventio.support.db.business

import com.example.venka.eventio.data.db.business.PlaceRepository
import com.example.venka.eventio.data.db.person.LegalPersonRepository
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

const val roomId = "1"

val place: Place = Place("1", "hello", "hello",
        rooms = mutableSetOf(Room(id = roomId, name = "1"))
)

val place2: Place = Place("2", "hello 2", "hello 2")

val person = LegalPerson("1", "hello", places = mutableSetOf(place))

/**
 * Bootstrap support class for Place testing
 */
@Component
class BootstrapPlace(
        private val placeRepository: PlaceRepository,
        private val personRepository: LegalPersonRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        placeRepository.save(place)
        placeRepository.save(place2)

        personRepository.save(person)
    }
}
