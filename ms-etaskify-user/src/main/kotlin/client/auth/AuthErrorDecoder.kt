package com.etaskify.ms.user.client.auth

import com.etaskify.ms.user.client.FeignClientErrorResponseDecoder
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder

class AuthErrorDecoder(
        private val objectMapper: ObjectMapper
) : ErrorDecoder {

    override fun decode(methodKey: String?, response: Response?): Exception {
        return FeignClientErrorResponseDecoder(
                objectMapper, "USER_CLIENT.UNEXPECTED_ERROR"
        ).decode(methodKey!!, response)
    }
}