package com.example.venka.eventio.controller.person

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.dto.person.LegalPersonDto
import com.example.venka.eventio.data.model.MessageType
import com.example.venka.eventio.exception.NotFoundException
import com.example.venka.eventio.exception.NotUniqueException
import com.example.venka.eventio.service.EventService
import com.example.venka.eventio.service.mail.MailService
import com.example.venka.eventio.service.person.LegalPersonService
import com.example.venka.eventio.translator.person.LegalPersonTranslator
import com.example.venka.eventio.utils.changeEmail
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import com.example.venka.eventio.utils.near
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.Optional
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Controller for {@link LegalPerson} entity
 */
@Monitor
@RestController
@RequestMapping("/legal-person")
class LegalPersonController(
        private val legalPersonService: LegalPersonService,
        private val legalPersonTranslator: LegalPersonTranslator,
        private val eventService: EventService,
        private val mailService: MailService
) : Logging by LoggingImpl<LegalPersonController>() {

    @GetMapping
    @Suppress("ReturnCount")
    fun getAll(
            @RequestParam locationFilter: Optional<Boolean>,
            authentication: Authentication
    ): List<LegalPersonDto> {
        log.debug("GET /legal-person => get all legalPersons")

        var list = legalPersonService.getAll().map { legalPersonTranslator.toDto(it) }

        locationFilter.ifPresent {
            if (!it) return@ifPresent

            log.debug("filter companies with locationFilter=$locationFilter")

            val location = (authentication.principal as UserAuthenticationProvider.PersonDetails).location
                    ?: return@ifPresent

            list = list.filter { it.places.any { it.near(location) == true } }
        }

        return list
    }

    @GetMapping("/{companyName}/places")
    fun getPlaces(@PathVariable companyName: String): MutableSet<PlaceDto> {
        log.debug("GET /legal-person/{companyName}/places => get places by company name: $companyName")

        val legalPerson = legalPersonService.getByCompanyUrl(companyName) ?: throw NotFoundException()
        val legalPersonDto = legalPersonTranslator.toDto(legalPerson)

        return legalPersonDto.places
    }

    @GetMapping("/places/{placeId}")
    fun getPlacesById(@PathVariable placeId: String, principal: Principal): PlaceDto? {
        log.debug("GET /legal-person/places/{placeId} => get place by $placeId " +
                "if it belongs to the user: ${principal.name}")

        val legalPerson = legalPersonService.getByParam(principal.name) ?: return null

        val legalPersonDto = legalPersonTranslator.toDto(legalPerson)
        return legalPersonDto.places.firstOrNull { it.id == placeId }
    }

    @GetMapping("/")
    fun getByParam(principal: Principal): LegalPersonDto {
        log.debug("GET /legal-person/ => get legalPerson by principal: ${principal.name}")

        val legalPerson = legalPersonService.getByParam(principal.name) ?: throw NotFoundException()
        return legalPersonTranslator.toDto(legalPerson)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): LegalPersonDto? {
        log.debug("GET /legal-person/$id => get legalPerson by id")

        val legalPerson = legalPersonService.getById(id) ?: throw NotFoundException()
        return legalPersonTranslator.toDto(legalPerson)
    }

    @PostMapping
    fun add(@RequestBody body: LegalPersonDto): LegalPersonDto? {
        log.debug("POST /legal-person => add legalPerson with body data")
        log.trace(body.toString())

        val legalPerson = legalPersonService.getByParam(body.email)

        whenNotNull(legalPerson) {
            log.error("Not unique legalPerson: ${it.email}")
            throw NotUniqueException()
        }

        var newLegalPerson = legalPersonTranslator.fromDto(body)
        newLegalPerson = legalPersonService.fixCompanyUrl(newLegalPerson)

        legalPersonService.save(newLegalPerson)

        log.trace("Added legalPerson: $newLegalPerson")

        return legalPersonTranslator.toDto(newLegalPerson)
    }

    @PutMapping
    fun save(@RequestBody body: LegalPersonDto, authentication: Authentication): LegalPersonDto? {
        log.debug("PUT /legal-person => update legalPerson with body data")
        log.trace(body.toString())

        val legalPerson = legalPersonService.getByParam(authentication.name) ?: throw NotFoundException()

        whenNotNull(legalPersonService.getByParam(body.email)) {
            if (authentication.name != it.email) {
                log.error("Not unique legalPerson: ${it.email}")
                throw NotUniqueException()
            }
        }

        var newLegalPerson = legalPersonTranslator.fromDto(body, legalPerson.id)
        newLegalPerson = legalPersonService.fixCompanyUrl(newLegalPerson)

        newLegalPerson.createdEvents = legalPerson.createdEvents
        newLegalPerson.places = legalPerson.places

        legalPersonService.save(newLegalPerson)

        if (newLegalPerson.email != authentication.name) {
            log.debug("change email:  ${authentication.name} => ${newLegalPerson.email}")
            authentication.changeEmail(newLegalPerson.email)
        }

        log.trace("Updated legalPerson: $newLegalPerson")

        return legalPersonTranslator.toDto(newLegalPerson)
    }

    @DeleteMapping("/")
    fun remove(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication)
            : LegalPersonDto? {
        val email = authentication.name
        log.debug("DELETE /legal-person/ => delete legalPerson by email: $email")

        val legalPerson = legalPersonService.getByParam(email) ?: throw NotFoundException()

        val events = eventService.setInvalidLabelByPerson(email)
        legalPersonService.deleteById(legalPerson.id!!)

        mailService.sendMessage(email, MessageType.INVALID_EVENT.simpleMessage, events)

        SecurityContextLogoutHandler().logout(request, response, authentication)

        log.trace("Deleted legalPerson: $legalPerson")

        return legalPersonTranslator.toDto(legalPerson)
    }
}
