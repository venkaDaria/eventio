package com.example.venka.eventio.utils.format

import com.fasterxml.jackson.databind.ObjectMapper
import org.json.simple.JSONObject
import javax.xml.bind.DatatypeConverter

private val mapper = ObjectMapper()

/**
 * Transforms any object to json
 */
fun Any.toJson(): String = mapper.writeValueAsString(this)

/**
 * Transforms any json to object
 */
fun <T> String.toObject(clazz: Class<T>): T = mapper.readValue(this, clazz)

/**
 * Transforms object map to json map
 */
fun <K, V> Map<K, V>.translateMap(): Map<String, V> {
    return entries.associateBy({ it.key!!.toJson() }, { it.value })
}

/**
 * Transforms String to JSONObject
 */
fun String.getJson(): JSONObject {
    return String(DatatypeConverter.parseBase64Binary(this)).toObject(JSONObject::class.java)
}
