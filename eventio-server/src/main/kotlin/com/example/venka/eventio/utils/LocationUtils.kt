package com.example.venka.eventio.utils

import com.example.venka.eventio.data.dto.business.PlaceDto
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.utils.format.toObject
import com.fasterxml.jackson.databind.JsonNode
import java.net.URL
import java.net.URLEncoder

private const val KEY = "xxx" // Google API

private const val BASE_URL = "https://maps.google.com/maps/api/geocode/json?key=$KEY&address="

private const val LIMIT = 200 // * 10^2 meters

/**
 * Checks if person locates near some place
 *
 * @return boolean value - if person locates near or not
 */
fun PlaceDto.near(location: String): Boolean {
    return isNear(realAddress, location)
}

fun Place.near(location: String): Boolean {
    return isNear(realAddress, location)
}

private fun isNear(address: String, address2: String): Boolean {
    val node = address.getLatLng()
    val node2 = address2.getLatLng()

    return node == node2 || node != null && node2 != null && node.near(node2)
}

private const val RADIUS = 6371e3 // meters

// ‘haversine’ formula
private fun JsonNode.near(latLng: JsonNode): Boolean {

    val lat1 = Math.toRadians(this["lat"].asDouble())
    val lat2 = Math.toRadians(latLng["lat"].asDouble())

    val lng1 = Math.toRadians(this["lng"].asDouble())
    val lng2 = Math.toRadians(latLng["lng"].asDouble())

    val deltaLat = Math.abs(Math.toRadians(lat2 - lat1))
    val deltaLng = Math.abs(Math.toRadians(lng2 - lng1))

    val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
            Math.cos(lat1) * Math.cos(lat2) *
            Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2)

    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return (RADIUS * c) < LIMIT
}

private fun String.getLatLng(): JsonNode? {
    val placeAddress = URLEncoder.encode(this, "UTF-8")

    val apiResponse = URL(BASE_URL + placeAddress).readText().toObject(JsonNode::class.java)
    val result = apiResponse["results"]

    return if (result.size() > 0) result[0]["geometry"]["location"] else null
}

