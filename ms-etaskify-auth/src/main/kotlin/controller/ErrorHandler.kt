package com.etaskify.ms.auth.controller

import com.etaskify.ms.auth.exception.AuthException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthException::class)
    fun handle(ex: AuthException): ErrorResponse {
        log.error("Authentication exception: ${ex.message}", ex)
        return ErrorResponse(
                code = ex.code,
                message = ex.message
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception): ErrorResponse {
        log.error("Unhandled exception message: {}", ex.message, ex)
        return ErrorResponse(
                code = HttpStatus.INTERNAL_SERVER_ERROR.name,
                message = HttpStatus.INTERNAL_SERVER_ERROR.name
        )
    }

}
data class ErrorResponse(
        val code: String,
        val message: String
)