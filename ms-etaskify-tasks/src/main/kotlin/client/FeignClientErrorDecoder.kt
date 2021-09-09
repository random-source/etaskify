package com.etaskify.ms.tasks.client

import com.etaskify.ms.tasks.exceptions.ClientException
import com.etaskify.ms.tasks.model.RestErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import org.slf4j.LoggerFactory
import java.io.IOException

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
