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
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

/**
 * Data Bootstrap for development.
 */
@Monitor
abstract class DevBootstrap(
        private val legalPersonRepository: LegalPersonRepository,
        private val naturalPersonRepository: NaturalPersonRepository,
        private val personRepository: PersonRepository,
        private val constraintController: ConstraintController,
        private val eventRepository: EventRepository,
        private val placeRepository: PlaceRepository,
        private val roomRepository: RoomRepository,
        private val featureRepository: FeatureRepository
) : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        deleteAll()
        constraintsExecute()

        specificActionExecute()
    }

    open fun specificActionExecute() {
        // default is nothing
    }

    private fun deleteAll() {
        personRepository.deleteAll()
        legalPersonRepository.deleteAll()
        naturalPersonRepository.deleteAll()
        eventRepository.deleteAll()
        placeRepository.deleteAll()
        roomRepository.deleteAll()
        featureRepository.deleteAll()
    }

    private fun constraintsExecute() {
        constraintController.emailConstraint()
        constraintController.titleConstraint()
        constraintController.addressConstraint()
    }
}
