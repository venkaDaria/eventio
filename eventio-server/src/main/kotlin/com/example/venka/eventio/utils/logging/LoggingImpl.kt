package com.example.venka.eventio.utils.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Class must be inherited for using logger-field.
 *
 * Example of usage: Logging by LoggingImpl<SomeClassName>()
 */
class LoggingImpl(loggerImpl: Logger) : Logging {

    override val log: Logger = loggerImpl

    companion object {
        inline operator fun <reified T> invoke(): LoggingImpl {
            return LoggingImpl(LoggerFactory.getLogger(T::class.java))
        }
    }
}
