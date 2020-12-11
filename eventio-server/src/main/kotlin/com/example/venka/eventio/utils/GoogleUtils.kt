package com.example.venka.eventio.utils

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.model.Userinfoplus

/**
 * Google utils (static methods).
 */
@Monitor
object GoogleUtils : Logging by LoggingImpl<GoogleUtils>() {

    /**
     * Check authorization with access token
     */
    fun isAuth(accessToken: String): Boolean {
        val credential = GoogleCredential()
                .setAccessToken(accessToken)

        val oauth2 = Oauth2.Builder(ApacheHttpTransport(), JacksonFactory(), credential)
                .setApplicationName("Oauth2")
                .build()

        val userInfo = oauth2.get()
        GoogleUtils.log.trace("get user information: $userInfo")

        return userInfo != null
    }
}

fun Oauth2.get(): Userinfoplus? {
    return try {
        userinfo().get().execute()
    } catch (ex: GoogleJsonResponseException) {
        null
    }
}
