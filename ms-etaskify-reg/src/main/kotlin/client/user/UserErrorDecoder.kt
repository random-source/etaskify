package com.etaskify.ms.reg.client.user

import com.etaskify.ms.reg.client.FeignClientErrorResponseDecoder
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder

class UserErrorDecoder(
        private val objectMapper: ObjectMapper
) : ErrorDecoder {

    override fun decode(methodKey: String?, response: Response?): Exception {
        return FeignClientErrorResponseDecoder(
                objectMapper, "USER_CLIENT.UNEXPECTED_ERROR"
        ).decode(methodKey!!, response)
    }
}