package com.example.venka.eventio.controller.complex

import com.example.venka.eventio.config.aop.Monitor
import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import com.liqpay.LiqPay
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.HashMap

private const val BASE_URL = "https://eventio.localtunnel.me"

/**
 * Controller for LigPay
 */
@Monitor
@RestController
@RequestMapping("/payment-form")
class PaymentController(private val liqPay: LiqPay) : Logging by LoggingImpl<PaymentController>() {

    /**
     * Returns LiqPay form for payment
     *
     * @param amount amount of payment
     * @param text description
     *
     * @return LiqPay form
     */
    @GetMapping
    fun getForm(@RequestParam amount: String, @RequestParam text: String, @RequestParam resultUrl: String,
                authentication: Authentication): String {
        log.debug("GET /payment-form with amount=$amount and description='$text'")

        val params = getMap(amount, text, resultUrl, authentication.name)
        log.debug(params.toString())

        return liqPay.cnb_form(params)
    }

    private fun getMap(
            amount: String,
            text: String,
            resultUrl: String,
            name: String
    ): HashMap<String, String> = with(HashMap<String, String>()) {
        this["currency"] = "UAH"

        // not real payment
        this["sandbox"] = "1"

        // send status payment
        this["server_url"] = "$BASE_URL/person/liqpay"

        this["amount"] = amount
        this["description"] = text
        this["result_url"] = resultUrl

        // save customer
        this["sender_first_name"] = name

        return this
    }
}
