package com.example.venka.eventio.utils.format

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

private val dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")

/**
 * Transforms LocalDateTime to String with formatter
 */
fun LocalDateTime.formatToString(): String = format(dateTimeFormatter)

/**
 * Transforms String to LocalDateTime with formatter
 */
fun String.parse(): LocalDateTime = LocalDateTime.parse(this, dateTimeFormatter)

/**
 * Returns now date and time without nanoseconds
 *
 * @return now LocalDateTime
 */
fun startDateTime(): LocalDateTime = LocalDateTime.now().withNano(0)


/**
 * Returns null if Optional<String> is empty or equals to "null"
 */
fun Optional<String>.getStringOrNull(): String? = this.map {
    if (it == "null") null else it
}.orElse(null)
