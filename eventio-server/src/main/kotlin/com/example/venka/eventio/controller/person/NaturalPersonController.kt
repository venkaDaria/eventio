package com.example.venka.eventio.controller.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.dto.person.NaturalPersonDto
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.exception.NotUniqueException
import com.example.venka.eventio.service.person.NaturalPersonService
import com.example.venka.eventio.translator.person.NaturalPersonTranslator
import com.example.venka.eventio.utils.changeEmail
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import com.example.venka.eventio.utils.whenNotNull
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Controller for {@link NaturalPerson} entity
 */
@Monitor
@RestController
@RequestMapping("/natural-person")
class NaturalPersonController(
        private val naturalPersonService: NaturalPersonService,
        private val naturalPersonTranslator: NaturalPersonTranslator
) : Logging by LoggingImpl<NaturalPersonController>() {

    @GetMapping
    fun getAll(): List<NaturalPersonDto> {
        log.debug("GET /natural-person => get all naturalPersons")

        return naturalPersonService.getAll().map { naturalPersonTranslator.toDto(it) }
    }

    @GetMapping("/")
    fun getByParam(principal: Principal): NaturalPersonDto? {
        log.debug("GET /natural-person/ => get naturalPerson by principal: ${principal.name}")

        val naturalPerson = naturalPersonService.getByParam(principal.name) ?: throw NotFoundException()
        return naturalPersonTranslator.toDto(naturalPerson)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): NaturalPersonDto? {
        log.debug("GET /natural-person/$id => get naturalPerson by id")

        val naturalPerson = naturalPersonService.getById(id) ?: throw NotFoundException()
        return naturalPersonTranslator.toDto(naturalPerson)
    }

    @PostMapping
    fun add(@RequestBody body: NaturalPersonDto): NaturalPersonDto? {
        log.debug("POST /natural-person => add naturalPerson with body data")
        log.trace(body.toString())

        val naturalPerson = naturalPersonService.getByParam(body.email)

        whenNotNull(naturalPerson) {
            log.error("Not unique naturalPerson: ${it.email}")
            throw NotUniqueException()
        }

        val newNaturalPerson = naturalPersonTranslator.fromDto(body)
        naturalPersonService.save(newNaturalPerson)

        log.trace("Added naturalPerson: $newNaturalPerson")

        return naturalPersonTranslator.toDto(newNaturalPerson)
    }

    @PutMapping
    fun save(@RequestBody body: NaturalPersonDto, authentication: Authentication): NaturalPersonDto? {
        log.debug("PUT /natural-person => update naturalPerson with body data")
        log.trace(body.toString())

        val naturalPerson = naturalPersonService.getByParam(authentication.name) ?: throw NotFoundException()

        whenNotNull(naturalPersonService.getByParam(body.email)) {
            if (authentication.name != it.email) {
                log.error("Not unique naturalPerson: ${it.email}")
                throw NotUniqueException()
            }
        }

        val newNaturalPerson = naturalPersonTranslator.fromDto(body, naturalPerson.id)

        newNaturalPerson.createdEvents = naturalPerson.createdEvents
        newNaturalPerson.subscribedEvents = naturalPerson.subscribedEvents

        naturalPersonService.save(newNaturalPerson)

        if (newNaturalPerson.email != authentication.name) {
            log.debug("change email:  ${authentication.name} => ${newNaturalPerson.email}")
            authentication.changeEmail(newNaturalPerson.email)
        }

        log.trace("Updated naturalPerson: $newNaturalPerson")

        return naturalPersonTranslator.toDto(newNaturalPerson)
    }

    @DeleteMapping("/")
    fun remove(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication)
            : NaturalPersonDto? {
        log.debug("DELETE /natural-person/ => delete naturalPerson by email: ${authentication.name}")

        val naturalPerson = naturalPersonService.getByParam(authentication.name)
                ?: throw NotFoundException()
        naturalPersonService.deleteById(naturalPerson.id!!)

        SecurityContextLogoutHandler().logout(request, response, authentication)

        log.trace("Deleted naturalPerson: $naturalPerson")

        return naturalPersonTranslator.toDto(naturalPerson)
    }
}
