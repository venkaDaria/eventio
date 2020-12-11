package com.example.venka.eventio.utils

import java.util.Random

private val random = Random()

private const val URL_LENGTH = 7

/**
 * Generates string as a company url in range 'a'..'z' with {@see URL_LENGTH}
 */
fun getUrlString() = ('a'..'z').randomString(URL_LENGTH)

private fun ClosedRange<Char>.randomString(length: Int) =
        (1..length)
                .map { (random.nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar() }
                .joinToString("")
