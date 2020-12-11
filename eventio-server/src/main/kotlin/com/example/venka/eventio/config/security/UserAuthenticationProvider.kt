package com.example.venka.eventio.config.security

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.data.db.person.PersonRepository
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.utils.GoogleUtils
import com.example.venka.eventio.utils.beautify
import com.example.venka.eventio.utils.getRoles
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import java.security.Principal

/**
 * Provides an user authentication logic.
 */
@Monitor
@Service
class UserAuthenticationProvider(private val personRepository: PersonRepository) : AuthenticationProvider,
        Logging by LoggingImpl<UserAuthenticationProvider>() {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        val email = authentication.name
        log.debug("try to login with email: $email")

        val token = authentication.credentials.toString()

        val person = personRepository.findByEmail(email)
        val location = (person as? NaturalPerson)?.location

        val roles = getRoles(person)

        return if (GoogleUtils.isAuth(token)) {
            log.debug("successful login with such roles: ${roles.beautify()}}")
            UsernamePasswordAuthenticationToken(PersonDetails(email, location), token, roles)
        } else {
            log.warn("unsuccessful login")
            null
        }
    }

    /**
     * Represented a object storing information for Spring Security (principal)
     */
    class PersonDetails(val email: String, val location: String? = null) : Principal {
        override fun getName(): String = email
    }

    override fun supports(authentication: Class<*>): Boolean =
            authentication == UsernamePasswordAuthenticationToken::class.java
}
