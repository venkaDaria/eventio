package com.example.venka.eventio.config.bootstrap

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.db.ConstraintController
import com.example.venka.eventio.data.db.EventRepository
import com.example.venka.eventio.data.db.business.FeatureRepository
import com.example.venka.eventio.data.db.business.PlaceRepository
import com.example.venka.eventio.data.db.business.RoomRepository
import com.example.venka.eventio.data.db.person.LegalPersonRepository
import com.example.venka.eventio.data.db.person.NaturalPersonRepository
import com.example.venka.eventio.data.db.person.PersonRepository
import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.MessageType
import com.example.venka.eventio.data.model.Mode
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Feature
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import com.example.venka.eventio.service.mail.MailService
import com.example.venka.eventio.service.mail.MailServiceImpl
import com.example.venka.eventio.utils.format.startDateTime
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

/**
 * Data Bootstrap for development (create action).
 */
@Monitor
@Configuration
@ConditionalOnProperty(name = ["database.recreated"], havingValue = "create")
class CreateBootstrap(
        private val legalPersonRepository: LegalPersonRepository,
        private val naturalPersonRepository: NaturalPersonRepository,
        personRepository: PersonRepository,
        constraintController: ConstraintController,
        private val eventRepository: EventRepository,
        private val placeRepository: PlaceRepository,
        roomRepository: RoomRepository,
        featureRepository: FeatureRepository,
        private val mailService: MailService
) : DevBootstrap(
        legalPersonRepository,
        naturalPersonRepository,
        personRepository,
        constraintController,
        eventRepository,
        placeRepository,
        roomRepository,
        featureRepository
), Logging by LoggingImpl<MailServiceImpl>() {

    override fun specificActionExecute() {
        val place = Place(name = "Парк им. Горького", realAddress = "Центральный парк культуры и отдыха " +
                "им. М. Горького, Sumska Street, Kharkiv, Харьковская область, Ukraine")

        val room = Room(name = "room_1")
        place.rooms.add(room)

        placeRepository.save(place)

        val place2 = Place(name = "Антикафе \"Коллаж\"", realAddress = "Антикафе \"Коллаж\", Knyazhyi Zaton Street, " +
                "Киев, Ukraine")

        val room2 = Room(name = "room_1")
        room2.features.add(Feature(name = "Wi-Fi"))
        room2.features.add(Feature(name = "вкусняшки"))

        place2.rooms.add(room2)

        placeRepository.save(place2)

        val event = Event(title = "Ice Cream Day", description = "...", mode = Mode.PUBLIC, location = room)
        eventRepository.save(event)

        val event2 = Event(title = "Мастер-класс по оригами", description = "...", mode = Mode.PUBLIC, location = room2,
                end = startDateTime().plusHours(2))
        eventRepository.save(event2)

        with(LegalPerson(email = "venka.daria@gmail.com", _name = "Testorria")) {
            createdEvents = mutableSetOf(event, event2)
            places = mutableSetOf(place2)

            legalPersonRepository.save(this)
        }

        with(NaturalPerson(email = "daria.pydorenko@nure.ua")) {
            subscribedEvents = mutableSetOf(event)

            naturalPersonRepository.save(this)
        }

        // test message
        try {
            mailService.sendMessage("5dwz52ps@stelliteop.info", MessageType.INVALID_EVENT.simpleMessage,
                    listOf("hello", "hello2"))
        } catch (ex: Exception) {
            log.error(ex.localizedMessage)
        }
    }
}
