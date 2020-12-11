package com.example.venka.eventio.utils

import com.example.venka.eventio.config.security.PersonAuthority
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.Person
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Transforms roles to string
 */
fun Collection<GrantedAuthority>.beautify() = joinToString { it.authority }

/**
 * Get roles for {@link Person}
 */
fun getRoles(person: Person?): Collection<GrantedAuthority> {
    return when (person) {
        is NaturalPerson -> PersonAuthority.natural
        is LegalPerson -> PersonAuthority.legal
        null -> PersonAuthority.natural
    }
}

/**
 * Change a principal object in an authentication object (Spring Security)
 */
fun Authentication.changeEmail(email: String) {
    setAuthentication(
            UserAuthenticationProvider.PersonDetails(
                    email,
                    (principal as UserAuthenticationProvider.PersonDetails).location
            ),
            credentials,
            authorities
    )
}

/**
 * Change roles in an authentication object (Spring Security)
 */
fun Authentication.changeRoles(isCompany: Boolean) {
    setAuthentication(
            principal as UserAuthenticationProvider.PersonDetails,
            credentials,
            if (isCompany) PersonAuthority.legal else PersonAuthority.natural
    )
}

/**
 * Set an authentication information to Spring context
 */
fun setAuthentication(
        personDetails: UserAuthenticationProvider.PersonDetails,
        credentials: Any?,
        authorities: Collection<GrantedAuthority>
) {
    val authentication = UsernamePasswordAuthenticationToken(
            personDetails,
            credentials,
            authorities
    )

    SecurityContextHolder.getContext().authentication = authentication
}
