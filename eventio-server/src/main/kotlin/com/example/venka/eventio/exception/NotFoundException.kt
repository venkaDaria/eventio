package com.example.venka.eventio.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

const val NOT_FOUND = "Not found a wanted object"

/**
 * Not found exception (404)
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
class NotFoundException : RuntimeException(NOT_FOUND)
