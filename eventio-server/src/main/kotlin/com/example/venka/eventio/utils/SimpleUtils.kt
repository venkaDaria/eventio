package com.example.venka.eventio.utils

/**
 * Do something when input is not null
 *
 * @param input can be null or not
 * @param callback some actions
 *
 * @return result of callback
 */
inline fun <T, R> whenNotNull(input: T?, callback: (T) -> R): R? = input?.let(callback)

/**
 * Deep equals for collections
 *
 * @param collection collection
 *
 * @return equal or not
 */
infix fun <T> Collection<T>.eq(collection: Collection<T>?) =
        collection != null && collection.size == size && collection.containsAll(this)
