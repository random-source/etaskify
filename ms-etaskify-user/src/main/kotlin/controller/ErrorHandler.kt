package com.etaskify.ms.user.controller

import com.etaskify.ms.user.exception.*
import com.etaskify.ms.user.model.RestErrorResponse
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserException::class)
    fun handle(ex: UserException): RestErrorResponse {
        log.error("Handling: ${ex.javaClass}: ${ex.localizedMessage}", ex)
        return RestErrorResponse(
                code = ex.code,
                message = ex.message
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserCreateException::class)
    fun handle(ex: UserCreateException): RestErrorResponse {
        log.error("Handling: ${ex.javaClass}: ${ex.localizedMessage}", ex)
        return RestErrorResponse(
                code = ex.code,
                message = ex.message
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OrganizationCreateException::class)
    fun handle(ex: OrganizationCreateException): RestErrorResponse {
        log.error("Handling: ${ex.javaClass}: ${ex.localizedMessage}", ex)
        return RestErrorResponse(
                code = ex.code,
                message = ex.message
        )
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrganizationException::class)
    fun handle(ex: OrganizationException): RestErrorResponse {
        log.error("Handling: ${ex.javaClass}: ${ex.localizedMessage}", ex)
        return RestErrorResponse(
                code = ex.code,
                message = ex.message
        )
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserAdminException::class)
    fun handle(ex: UserAdminException): RestErrorResponse {
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
                code = ex.code!!,
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
