package com.japanet.menuapi.utils.log

import mu.KLogger
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect(
    private val log: KLogger = KotlinLogging.logger {}
) {

    @Pointcut("@annotation(Logging)")
    fun logPointcut() {}

    @Before("logPointcut()")
    fun logAllMethodCallsAdvice(joinPoint: JoinPoint) {
        val args = joinPoint.args.map { it }
        log.info { "C=${joinPoint.target::class.simpleName}, M=${joinPoint.signature.name}, args=${args}" }
    }

    @AfterReturning(value = "logPointcut()", returning = "returnValue")
    fun logAfterMethodCallAdvice(joinPoint: JoinPoint, returnValue: Any) {
        log.info { "C=${joinPoint.target::class.simpleName}, " +
                "M=${joinPoint.signature.name}, " +
                "${returnValue}" }
    }
}
