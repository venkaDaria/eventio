package com.example.venka.eventio.config

import com.example.venka.eventio.config.security.RestAuthenticationEntryPoint
import com.example.venka.eventio.config.security.RestAuthenticationSuccessHandler
import com.example.venka.eventio.config.security.UserAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

private const val FAVICON_ICO = "/favicon.ico"
private const val LOGIN_PAGE = "/login"
private const val EVENT_PAGE = "/event"
private const val RESOURCES = "/resources/templates/**"
private const val LIQ_PAY = "/person/liqpay"
private const val ALL = "/**"

/**
 * Spring Security configuration.
 */
@Configuration
@EnableWebSecurity
class SecurityConfiguration(
        private val authenticationEntryPoint: RestAuthenticationEntryPoint,
        private val successHandler: RestAuthenticationSuccessHandler,
        private val failureHandler: SimpleUrlAuthenticationFailureHandler
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .cors().and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(authenticationEntryPoint,
                        AntPathRequestMatcher(except(LOGIN_PAGE))
                )
                .and()
                .authorizeRequests()
                .antMatchers(RESOURCES, LOGIN_PAGE).permitAll()
                .antMatchers(HttpMethod.OPTIONS, ALL).permitAll()
                .antMatchers(HttpMethod.GET, FAVICON_ICO, EVENT_PAGE).permitAll()
                .antMatchers(HttpMethod.POST, LIQ_PAY).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(LOGIN_PAGE)
                .usernameParameter("email")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
    }

    private fun except(page: String): String? = "^/$page"

    /**
     * Sets an authentication provider.
     */
    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder, provider: UserAuthenticationProvider) {
        auth.authenticationProvider(provider)
    }
}
