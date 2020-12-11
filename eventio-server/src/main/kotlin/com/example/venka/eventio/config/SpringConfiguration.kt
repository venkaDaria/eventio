package com.example.venka.eventio.config

import com.liqpay.LiqPay
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler

/**
 * Beans configuration.
 */
@Configuration
@PropertySource("classpath:secret.properties")
class SpringConfiguration {

    @Autowired
    private lateinit var env: Environment

    /**
     * Returns a failure handler
     *
     * @return SimpleUrlAuthenticationFailureHandler
     */
    @Bean
    fun myFailureHandler(): SimpleUrlAuthenticationFailureHandler = SimpleUrlAuthenticationFailureHandler()

    /**
     * Returns a public key for LiqPay
     *
     * @return public key for LiqPay
     */
    @Bean
    fun keyPublic(): String? = env.getProperty("liqpay.key.public")

    /**
     * Returns a private key for LiqPay
     *
     * @return private key for LiqPay
     */
    @Bean
    fun keyPrivate(): String? = env.getProperty("liqpay.key.private")

    /**
     * Returns a LiqPay api object
     *
     * @param keyPrivate private key
     * @param keyPublic public key
     * @return LiqPay
     */
    @Bean
    fun liqPay(keyPrivate: String, keyPublic: String) = LiqPay(keyPublic, keyPrivate)
}
