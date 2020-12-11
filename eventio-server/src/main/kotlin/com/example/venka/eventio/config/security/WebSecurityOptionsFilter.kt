package com.example.venka.eventio.config.security

import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Filters OPTIONS requests and sets necessary headers.
 */
@Component
class WebSecurityOptionsFilter : Filter, Logging by LoggingImpl<WebSecurityOptionsFilter>() {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, filterChain: FilterChain) {
        val request = req as HttpServletRequest

        val response = res as HttpServletResponse
        response.setHeaders()

        if ("OPTIONS" == request.method) {
            log.trace("${request.method} ${request.requestURI}")
            response.status = HttpServletResponse.SC_OK
        } else {
            filterChain.doFilter(request, response)
        }
    }

    override fun destroy() {
    }
}

/**
 * Url of web-client (website)
 */
const val WEB_URL: String = "http://localhost:3000"

/**
 * Creates headers for CORS configuration
 */
fun HttpServletResponse.setHeaders() {
    setHeader("Access-Control-Allow-Origin", WEB_URL)
    setHeader("Access-Control-Allow-Credentials", "true")
    setHeader("Access-Control-Allow-Methods", "POST, PUT, DELETE, GET, HEAD, OPTIONS")
    setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, " +
            "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers")
}
