package com.example.venka.eventio.config.security

import org.springframework.security.core.GrantedAuthority

/**
 * Represents an authority granted to a {@link Person} object.
 */
class PersonAuthority private constructor(private val role: Role) : GrantedAuthority {

    override fun getAuthority(): String {
        return role.name
    }

    private enum class Role {
        NATURAL_PERSON, LEGAL_PERSON
    }

    /**
     * Static methods
     */
    companion object {
        private val naturalAuthorities = setOf(PersonAuthority(Role.NATURAL_PERSON))
        private val legalAuthorities = setOf(PersonAuthority(Role.LEGAL_PERSON))

        /**
         * Returns roles for an {@link NaturalPerson} object.
         *
         * @return NaturalPerson roles
         */
        internal val natural: Collection<GrantedAuthority>
            get() = naturalAuthorities

        /**
         * Returns roles for an {@link LegalPerson} object.
         *
         * @return LegalPerson roles
         */
        internal val legal: Collection<GrantedAuthority>
            get() = legalAuthorities
    }
}
