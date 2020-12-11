package com.example.venka.eventio.config.security

import com.example.venka.eventio.utils.format.toJson
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.security.core.Authentication
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Strategy used to handle a successful user authentication.
 */
@Component
class RestAuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler(),
        Logging by LoggingImpl<RestAuthenticationSuccessHandler>() {
    init {
        redirectStrategy = NoRedirectStrategy()
    }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                         authentication: Authentication) {
        super.onAuthenticationSuccess(request, response, authentication)

        with(response) {
            setHeaders()
            writer.print(authentication.toJson())
        }

        log.info("${request.method} ${request.requestURI} => successful user authentication")
    }

    /**
     * Represented no redirect strategy.
     */
    protected inner class NoRedirectStrategy : RedirectStrategy {
        @Throws(IOException::class)
        override fun sendRedirect(request: HttpServletRequest,
                                  response: HttpServletResponse, url: String) {
            // no redirect
        }
    }
}
