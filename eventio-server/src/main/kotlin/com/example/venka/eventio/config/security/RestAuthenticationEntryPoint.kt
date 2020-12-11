package com.example.venka.eventio.config.security

import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * An entry point for all unauthorized requests.
 */
@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint, Logging by LoggingImpl<RestAuthenticationEntryPoint>() {

    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse,
                          authException: AuthenticationException) {
        with(response) {
            setHeaders()
            sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }

        log.warn("${request.method} ${request.requestURI} => unauthorized request")
    }
}
