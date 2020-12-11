package com.example.venka.eventio.service

/**
 * Service
 *
 * T - type of an entity
 * R - type of an entity.id
 */
interface Service<T, in R> {

    /**
     * Get all entities
     *
     * @return list of entities
     */
    fun getAll(): List<T>

    /**
     * Save entity
     *
     * @param entity
     */
    fun save(entity: T): T

    /**
     * Delete entity by id
     *
     * @param id
     */
    fun deleteById(id: R)

    /**
     * Get entity by id
     *
     * @param id
     *
     * @return entity
     */
    fun getById(id: R): T?

    /**
     * Get entity by param
     *
     * @param param string param value
     *
     * @return entity
     */
    fun getByParam(param: String): T?

    /**
     * Delete all entities
     */
    fun deleteAll()
}
