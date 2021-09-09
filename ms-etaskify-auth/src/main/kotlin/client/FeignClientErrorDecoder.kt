package com.etaskify.ms.auth.client

import com.etaskify.ms.auth.exception.ClientException
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import org.slf4j.LoggerFactory
import java.io.IOException

import com.fasterxml.jackson.annotation.JsonInclude

class FeignClientErrorResponseDecoder(
        private val objectMapper: ObjectMapper,
        private val unexpectedErrorMessage: String
) {

    companion object {
        private var log = LoggerFactory.getLogger(this::class.java)
    }

    fun decode(methodKey: String, response: Response?): Exception {
        try {
            if (response?.body() == null) {
                log.warn(
                        "ActionLog.decode.warn: Status {} reading {}, empty response body",
                        response?.status(),
                        methodKey
                )

                return ClientException(unexpectedErrorMessage)
            }

            val restError = objectMapper.readValue(response.body().asInputStream(), RestErrorResponse::class.java)

            log.warn("ActionLog.decode.warn: Status {} reading {}, message: {}",
                    response.status(),
                    methodKey,
                    restError.message
            )

            return ClientException(
                    message = restError.message,
                    code = restError.code
            )
        } catch (ex: IOException) {
            log.error("ActionLog.decode.error: {}, {}", ex.message, ex)
            return ClientException(unexpectedErrorMessage)
        }
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RestErrorResponse(
        val code: String? = null,
        val message: String,
        val httpCode: Int? = null,
        val isError: Boolean? = null
)