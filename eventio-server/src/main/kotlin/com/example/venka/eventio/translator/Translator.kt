package com.example.venka.eventio.translator

/**
 * Translator
 *
 * T - type of an entity
 * R - type of a dto
 * K - type of an entity id
 */
interface Translator<T, R, in K> {

    /**
     * Translates source to dto with id
     *
     * @param source
     * @param id id for setting in dto (if id null, sets source.id)
     */
    fun fromDto(source: R, id: K?): T

    /**
     * Translates source to dto with id
     *
     * @param source
     */
    fun fromDto(source: R): T = fromDto(source, null)

    /**
     * Translates source to entity from dto
     */
    fun toDto(source: T): R
}
