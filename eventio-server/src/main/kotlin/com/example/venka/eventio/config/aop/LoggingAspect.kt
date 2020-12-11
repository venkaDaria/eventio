package com.example.venka.eventio.config.aop

import com.example.venka.eventio.utils.logging.Logging
import com.example.venka.eventio.utils.logging.LoggingImpl
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Component
@Aspect
class LoggingAspect : Logging by LoggingImpl<LoggingAspect>() {

    @Suppress("unused")
    @Pointcut("within(@com.example.venka.eventio.config.aop.Monitor *)")
    private fun allInPackage() {
    }

    @Around("allInPackage()")
    @Throws(Throwable::class)
    fun aroundLog(joinPoint: ProceedingJoinPoint): Any? {
        log.trace("[around] ${joinPoint.signature.toShortString()} started")

        val result = joinPoint.proceed()

        log.trace("[around] ${joinPoint.signature.toShortString()} ended")
        return result
    }

    @AfterReturning(pointcut = "allInPackage()", returning = "result")
    fun afterReturningLog(joinPoint: JoinPoint, result: Any?) {
        log.trace("[after return] ${joinPoint.signature.toShortString()} returned value is: " + result)
    }

    @AfterThrowing(pointcut = "allInPackage()", throwing = "error")
    fun afterThrowingLog(joinPoint: JoinPoint, error: Throwable) {
        log.trace("[after throw] Exception in ${joinPoint.signature.toShortString()}: " + error)
    }
}
