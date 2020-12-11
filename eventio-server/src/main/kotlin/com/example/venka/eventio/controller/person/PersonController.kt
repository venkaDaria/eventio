package com.example.venka.eventio.controller.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.config.security.PersonAuthority.Companion.natural
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.EventDto
import com.example.venka.eventio.data.dto.person.PersonDto
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.exception.NotUniqueException
import com.example.venka.eventio.service.person.PersonService
import com.example.venka.eventio.translator.EventTranslator
import com.example.venka.eventio.translator.person.PersonTranslator
import com.example.venka.eventio.utils.beautify
import com.example.venka.eventio.utils.format.getJson
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import com.example.venka.eventio.utils.whenNotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Controller for {@link Person} entity
 */
@Monitor
@RestController
@RequestMapping("/person")
class PersonController(
        private val personService: PersonService,
        private val personTranslator: PersonTranslator,
        private val eventTranslator: EventTranslator
) : Logging by LoggingImpl<PersonController>() {

    @GetMapping
    fun getAll(): List<PersonDto> {
        log.debug("GET /person => get all persons")

        return personService.getAll().map { personTranslator.toDto(it) }
    }

    @GetMapping("/")
    fun getByParam(principal: Principal): PersonDto? {
        log.debug("GET /person/ => get person by principal: ${principal.name}")

        val person = personService.getByParam(principal.name) ?: throw NotFoundException()
        return personTranslator.toDto(person)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): PersonDto? {
        log.debug("GET /person/$id => get person by id")

        val person = personService.getById(id) ?: throw NotFoundException()
        return personTranslator.toDto(person)
    }

    @PostMapping
    fun add(@RequestBody body: PersonDto): PersonDto? {
        log.debug("POST /person => add person with body data")
        log.trace(body.toString())

        val person = personService.getByParam(body.email)

       whenNotNull(person) {
            // do not throw exception (for sign in)
            log.warn("Not unique person: ${it.email}")

            return personTranslator.toDto(it)
        }

        val newPerson = personTranslator.fromDto(body)
        personService.save(newPerson)

        log.trace("Added person: $newPerson")

        return personTranslator.toDto(newPerson)
    }

    @PutMapping
    fun save(@RequestBody body: PersonDto, principal: Principal): PersonDto? {
        log.debug("PUT /person => update person with body data")
        log.trace(body.toString())

        val person = personService.getByParam(principal.name) ?: throw NotFoundException()

        whenNotNull(personService.getByParam(body.email)){
            if (principal.name != it.email) {
                log.error("Not unique person: ${it.email}")
                throw NotUniqueException()
            }
        }

        val newPerson = personTranslator.fromDto(body, person.id)
        newPerson.createdEvents = person.createdEvents

        personService.save(newPerson)

        log.trace("Updated person: $newPerson")

        return personTranslator.toDto(newPerson)
    }

    @PostMapping("/liqpay")
    @PreAuthorize("@LiqPaySecurity.validData(keyPrivate + #data + keyPublic, #signature)")
    fun toggleWithPay(@RequestParam data: String, @RequestParam signature: String)
            : PersonStub {
        log.debug("Payment data: $data, signature: $signature")

        val email: String = data.getJson()["sender_first_name"].toString()

        return toggle(UsernamePasswordAuthenticationToken(UserAuthenticationProvider.PersonDetails(email),
                null, natural))
    }

    @PostMapping("/label")
    fun toggle(authentication: Authentication): PersonStub {
        log.debug("POST /person/label => set LegalPerson or not: ${authentication.authorities.beautify()}")

        personService.toggleLabel(authentication)

        return getLabel(authentication)
    }

    @GetMapping("/label")
    fun getLabel(authentication: Authentication): PersonStub {
        log.debug("GET /person/label => check is LegalPerson or not by principal: ${authentication.name}")

        val byParam = personService.getByParam(authentication.name) ?: throw NotFoundException()

        val events = byParam.createdEvents
        if (byParam is NaturalPerson) {
            events.addAll(byParam.subscribedEvents)
        }

        return PersonStub(authentication.name, byParam is LegalPerson, events.map {
            eventTranslator.toDto(it)
        }.toMutableSet())
    }

    data class PersonStub(val email: String, val isCompany: Boolean, val events: MutableSet<EventDto>?)

    @DeleteMapping("/")
    fun remove(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): PersonDto? {
        log.debug("DELETE /person/ => delete person by email: ${authentication.name}")

        val person = personService.getByParam(authentication.name) ?: throw NotFoundException()
        personService.deleteById(person.id!!)

        SecurityContextLogoutHandler().logout(request, response, authentication)

        log.trace("Deleted person: $person")

        return personTranslator.toDto(person)
    }
}
