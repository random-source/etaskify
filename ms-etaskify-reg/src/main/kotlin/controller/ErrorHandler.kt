package com.etaskify.ms.reg.controller

import com.etaskify.ms.reg.exceptions.ClientException
import com.etaskify.ms.reg.exceptions.RegistrationException
import com.etaskify.ms.reg.model.RestErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    companion object {
        private val log = LoggerFactory.getLogger(ErrorHandler::class.java)
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(RegistrationException::class)
    fun handle(ex: RegistrationException): RestErrorResponse {
        log.error("Handling: ${ex.javaClass}: ${ex.localizedMessage}", ex)
        return RestErrorResponse(
            code = ex.code,
            message = ex.message
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ClientException::class)
    fun handle(ex: ClientException): RestErrorResponse {
        log.error("ActionLog.FeignClientException.error: ${ex.message}")
        return RestErrorResponse(
                code = ex.code,
                message = ex.message
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception): RestErrorResponse {
        val response = RestErrorResponse("error.unexpected", "Unknown error occurred")
        log.error("Handling: ${ex.javaClass}: ${ex.localizedMessage}", ex)
        return RestErrorResponse(
                code = response.code,
                message = response.message
        )
    }

}
