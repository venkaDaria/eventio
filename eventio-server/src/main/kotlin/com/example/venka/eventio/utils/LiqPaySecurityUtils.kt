package com.example.venka.eventio.utils

import com.liqpay.LiqPayUtil.base64_encode
import com.liqpay.LiqPayUtil.sha1
import org.springframework.stereotype.Service

/**
 * LiqPay security
 */
interface LiqPaySecurity {

    /**
     * Checks if this data sent by LiqPay or not
     *
     * @param param specific data (with public and private keys)
     * @param signature LiqPay signature
     */
    fun validData(param: String, signature: String): Boolean
}

/**
 * Implementation of {@link LiqPaySecurity}
 */
@Service("LiqPaySecurity")
class LiqPaySecurityImpl : LiqPaySecurity {

    override fun validData(param: String, signature: String): Boolean {
        return base64_encode(sha1(param)) == signature
    }
}
