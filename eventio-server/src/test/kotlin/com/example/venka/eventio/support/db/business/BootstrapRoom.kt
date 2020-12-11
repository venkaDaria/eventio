package com.example.venka.eventio.support.db.business

import com.example.venka.eventio.data.db.business.RoomRepository
import com.example.venka.eventio.data.model.business.Room
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

val room: Room = Room("1", "hello")

val room2: Room = Room("2", "hello 2")

/**
 * Bootstrap support class for Room testing
 */
@Component
class BootstrapRoom(private val roomRepository: RoomRepository) : CommandLineRunner {

    override fun run(vararg args: String) {
        roomRepository.save(room)
        roomRepository.save(room2)
    }
}
