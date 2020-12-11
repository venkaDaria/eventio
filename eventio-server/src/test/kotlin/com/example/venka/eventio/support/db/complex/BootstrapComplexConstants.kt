package com.example.venka.eventio.support.db.complex

import com.example.venka.eventio.data.model.Event
import com.example.venka.eventio.data.model.LegalPerson
import com.example.venka.eventio.data.model.NaturalPerson
import com.example.venka.eventio.data.model.business.Place
import com.example.venka.eventio.data.model.business.Room
import java.time.LocalDateTime

const val id = "1"

const val id2 = "2"

const val id3 = "3"

const val helloEmail = "hello3@gmail.com"

val place = Place(id = id, realAddress = "my place")

val room = Room(id = id2, name = "room")

val room2 = Room(id = id3, name = "room2")

val naturalPerson = NaturalPerson(id, "hello@gmail.com", "123")

val naturalPerson2 = NaturalPerson(id2, "hello2@gmail.com", "456")

val legalPerson = LegalPerson(id3, helloEmail, "789")

val event = Event(id, "hello", location = room, end = LocalDateTime.now().plusHours(3))

val event2 = Event(id2, "hello 2", location = room2)