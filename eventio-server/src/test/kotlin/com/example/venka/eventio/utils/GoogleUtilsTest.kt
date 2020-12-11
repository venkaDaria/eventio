package com.example.venka.eventio.utils

import com.example.venka.eventio.support.fileName
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.model.Userinfoplus
import io.mockk.every
import io.mockk.staticMockk
import io.mockk.use
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class GoogleUtilsTest {

    @Test
    fun testGoogleUtils_Success() {
        staticMockk(GoogleUtils.fileName()).use {
            every {
                any<Oauth2>().get()
            } answers {
                Userinfoplus()
            }

            assertTrue(GoogleUtils.isAuth("good token"))
        }
    }

    @Test
    fun testGoogleUtils_False() {
        assertFalse(GoogleUtils.isAuth("wrong token"))
    }
}
