package com.example.venka.eventio.service.person

import com.example.venka.eventio.data.model.Person
import com.example.venka.eventio.service.Service
import org.springframework.security.core.Authentication

/**
 * Service for {@link Person}
 */
interface PersonService : Service<Person, String> {

    /**
     * Changes NaturalPerson to LegalPerson and vice versa
     *
     * @param authentication authentication details (with Person principal)
     */
    fun toggleLabel(authentication: Authentication)
}
