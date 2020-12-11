package com.example.venka.eventio.data.model

/**
 * Parent for all entities for /GET with param
 */
abstract class Entity<T, out R>(open var id: T? = null) {

    abstract fun getParam(): R
}
