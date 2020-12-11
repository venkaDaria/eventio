package com.example.venka.eventio.utils

import com.example.venka.eventio.data.model.Event
import java.time.LocalDateTime

/**
 * Checks if the event don't match a specific time
 *
 * @param startDateTime a start time to check
 * @param endDateTime an end time to check
 *
 * @return true if it is not the same time and false if is
 */
fun Event.notTheSameTime(startDateTime: LocalDateTime, endDateTime: LocalDateTime?)
        : Boolean {
    return end != null && end!! <= startDateTime ||
            end == null && endDateTime != null
            && notTheSameTimeWithNullEnd(start, startDateTime, endDateTime) ||
            endDateTime != null && start >= endDateTime ||
            end != null && endDateTime == null
            && notTheSameTimeWithNullEnd(startDateTime, start, end!!) ||
            end == null && endDateTime == null
            && startDateTime != start
}

private fun notTheSameTimeWithNullEnd(start: LocalDateTime, startDateTime: LocalDateTime, endDateTime: LocalDateTime) =
        start < startDateTime && start < endDateTime || start > startDateTime && start >= endDateTime
