package com.example.venka.eventio.utils

import com.example.venka.eventio.config.security.PersonAuthority.Companion.legal
import com.example.venka.eventio.config.security.PersonAuthority.Companion.natural
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import io.mockk.spyk
import io.mockk.verify
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class SecurityUtilsTest {

    private val authentication = spyk(UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello@gmail.com"), null, natural))

    private val authentication2 = spyk(UsernamePasswordAuthenticationToken(
            UserAuthenticationProvider.PersonDetails("hello2@gmail.com"), null, legal))

    private val badAuthentication = UsernamePasswordAuthenticationToken("hello2", null, natural)

    @Test
    fun testGetRoles_Legal() {
        assertEquals(getRoles(LegalPerson()), legal)
    }

    @Test
    fun testGetRoles_Natural() {
        assertEquals(getRoles(NaturalPerson()), natural)
    }

    @Test
    fun testGetRoles_Null() {
        assertEquals(getRoles(null), natural)
    }

    @Test
    fun testChangeEmail() {
        val newEmail = "new@gmail.com"
        authentication.changeEmail(newEmail)

        val newDetails = UserAuthenticationProvider.PersonDetails(newEmail)

        verify {
            setAuthentication(newDetails, authentication.credentials, natural)
        }
    }

    @Test(expectedExceptions = [ClassCastException::class])
    fun testChangeEmail_NotPersonDetails() {
        badAuthentication.changeEmail("hello")
    }

    @Test
    fun testChangeRoles() {
        authentication.changeRoles(true)

        verify {
            setAuthentication(authentication.principal as UserAuthenticationProvider.PersonDetails,
                    authentication.credentials, legal)
        }

        authentication2.changeRoles(false)

        verify {
            setAuthentication(authentication2.principal as UserAuthenticationProvider.PersonDetails,
                    authentication2.credentials, natural)
        }
    }

    @Test(expectedExceptions = [ClassCastException::class])
    fun testChangeRoles_NotPersonDetails() {
        badAuthentication.changeRoles(true)
    }
}