package com.example.venka.eventio.support

import com.example.venka.eventio.controller.GlobalExceptionHandler
import com.example.venka.eventio.exception.NOT_FOUND
import com.example.venka.eventio.exception.NOT_UNIQUE
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

private const val NOT_EXPECTED: String = "Unexpected exception"

val ERROR_NOT_UNIQUE = GlobalExceptionHandler.ErrorResponse(INTERNAL_SERVER_ERROR, NOT_UNIQUE)

val ERROR_NOT_FOUND = GlobalExceptionHandler.ErrorResponse(INTERNAL_SERVER_ERROR, NOT_FOUND)

val ERROR_UNEXPECTED = GlobalExceptionHandler.ErrorResponse(INTERNAL_SERVER_ERROR, NOT_EXPECTED)
