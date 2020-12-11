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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

/**
 * Data Bootstrap for development (clean action).
 */
@Monitor
@Configuration
@ConditionalOnProperty(name = ["database.recreated"], havingValue = "clean")
class CleanBootstrap(
        legalPersonRepository: LegalPersonRepository,
        naturalPersonRepository: NaturalPersonRepository,
        personRepository: PersonRepository,
        constraintController: ConstraintController,
        eventRepository: EventRepository,
        placeRepository: PlaceRepository,
        roomRepository: RoomRepository,
        featureRepository: FeatureRepository
) : DevBootstrap(
        legalPersonRepository,
        naturalPersonRepository,
        personRepository,
        constraintController,
        eventRepository,
        placeRepository,
        roomRepository,
        featureRepository
)
