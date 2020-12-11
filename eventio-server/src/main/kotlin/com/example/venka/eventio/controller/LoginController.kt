package com.example.venka.eventio.controller

import com.example.venka.eventio.config.aop.Monitor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Monitor
@Controller
class LoginController {

    @GetMapping("/login")
    fun login(): String = "login"

    @GetMapping("/")
    fun swagger(): String = "redirect:/swagger-ui.html#/"
}
