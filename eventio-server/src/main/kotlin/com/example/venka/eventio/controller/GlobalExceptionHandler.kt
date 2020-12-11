package com.example.venka.eventio.controller

import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Handles all exceptions
 */
@RestControllerAdvice
class GlobalExceptionHandler : Logging by LoggingImpl<GlobalExceptionHandler>() {

    /**
     * Handles Throwable exceptions
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable::class)
    fun handleThrowable(ex: Throwable): GlobalExceptionHandler.ErrorResponse {
        log.error("Unexpected error", ex)

        return ErrorResponse(INTERNAL_SERVER_ERROR, ex.localizedMessage)
    }

    /**
     * Stores en error code and a message
     */
    data class ErrorResponse(
            val code: HttpStatus,
            val message: String? = null
    )
}
