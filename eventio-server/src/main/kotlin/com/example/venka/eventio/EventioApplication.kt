package com.example.venka.eventio

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class EventioApplication

/**
 * Entry point
 */
fun main(args: Array<String>) {

    @Suppress("SpreadOperator")
    SpringApplication.run(EventioApplication::class.java, *args)
}
