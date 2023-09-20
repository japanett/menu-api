package com.japanet.menuapi.controller.advice

import com.japanet.menuapi.exception.CategoryNotFoundException
import com.japanet.menuapi.exception.DuplicateEstablishmentException
import com.japanet.menuapi.exception.MenuNotFoundException
import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class MenuapiAdvice(
    private val log: KLogger = KotlinLogging.logger {}
) : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.error { "C=${this::class.simpleName}, M=${this::handleMethodArgumentNotValid.name}, e=${ex}" }
        val errors = ex.bindingResult.fieldErrors
            .map { ErrorResponse("Valor invalido para o campo ${it.field} (${it.defaultMessage})") }
            .toList()
        return ResponseEntity.status(status).body(ErrorResponseWrapper(errors))
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.error { "C=${this::class.simpleName}, M=${this::handleHttpMessageNotReadable.name}, e=${ex}" }
        return ResponseEntity.status(status).body(ErrorResponseWrapper(listOf(ErrorResponse("Missing parameters"))))
    }

    @ExceptionHandler(Exception::class)
    fun handleUncaughtException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponseWrapper> {
        log.error { "C=${this::class.simpleName}, M=${this::handleUncaughtException.name}, e=${ex}" }
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponseWrapper(listOf(ErrorResponse("Erro interno"))))
    }

    @ExceptionHandler(value = [
        MenuNotFoundException::class,
        CategoryNotFoundException::class
    ])
    fun handleNotFoundException(ex: Exception): ResponseEntity<ErrorResponseWrapper> {
        log.error { "C=${this::class.simpleName}, M=${this::handleNotFoundException.name}, e=${ex}" }
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponseWrapper(listOf(ErrorResponse(ex.message))))
    }

    @ExceptionHandler(value = [DuplicateEstablishmentException::class])
    fun handleDuplicatedDataException(ex: Exception): ResponseEntity<ErrorResponseWrapper> {
        log.error { "C=${this::class.simpleName}, M=${this::handleDuplicatedDataException.name}, e=${ex}" }
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ErrorResponseWrapper(listOf(ErrorResponse(ex.message))))
    }

    @ExceptionHandler(value = [
        IllegalArgumentException::class
    ])
    fun handleIllegalArgumentException(ex: Exception): ResponseEntity<ErrorResponseWrapper> {
        log.error { "C=${this::class.simpleName}, M=${this::handleIllegalArgumentException.name}, e=${ex}" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseWrapper(listOf(ErrorResponse("Parametros invalidos"))))
    }
}

data class ErrorResponseWrapper(val errors: List<ErrorResponse>)

data class ErrorResponse(val message: String?)
