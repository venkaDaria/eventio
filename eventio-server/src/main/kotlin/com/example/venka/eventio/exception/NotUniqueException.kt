package com.example.venka.eventio.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

const val NOT_UNIQUE = "This object is not unique"

/**
 * Not unique exception (404)
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Unique")
class NotUniqueException : RuntimeException(NOT_UNIQUE)
