package com.japanet.menuapi.utils.log

import mu.KLogger
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.CodeSignature
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect(
    private val log: KLogger = KotlinLogging.logger {}
) {

    companion object {
        private val pattern = """(?<=password=|email=|phone=)(?!null).*?(?=\Snull|@|(,\s)|(\)$)|$)""".toRegex()
        private const val REPLACEMENT = "********"
    }

    @Pointcut("@annotation(Logging)")
    fun logPointcut() {}

    @Before("logPointcut()")
    fun logAllMethodCallsAdvice(joinPoint: JoinPoint) {
        val signature: CodeSignature = joinPoint.signature as CodeSignature
        var stringParams = ""

        signature.parameterNames.forEachIndexed { index, param ->
            val arg = joinPoint.args[index]
            stringParams = if (index != 0) "$stringParams, ${param}=${arg}"
            else "${param}=${joinPoint.args[index]}"
        }

        log.info { "C=${joinPoint.target::class.simpleName}, M=${signature.name}, P=[${stringParams.replace(pattern, REPLACEMENT)}]" }
    }

    @AfterReturning(value = "logPointcut()", returning = "returnValue")
    fun logAfterMethodCallAdvice(joinPoint: JoinPoint, returnValue: Any?) {
        log.info { "C=${joinPoint.target::class.simpleName}, " +
                "M=${joinPoint.signature.name}, " +
                "R=[${returnValue.toString().replace(pattern, REPLACEMENT)}]"
        }
    }
}
