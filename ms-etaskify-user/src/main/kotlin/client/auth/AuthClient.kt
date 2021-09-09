package com.etaskify.ms.user.client.auth

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "ms-etaskify-auth", url = "\${client.auth.url}", configuration = [AuthErrorDecoder::class])
interface AuthClient {

    @PostMapping("/v1/internal/auth/password")
    fun generatePassword(@RequestBody req: PasswordGenerationReqDto): PasswordGenerationRespDto

}

data class PasswordGenerationReqDto (
        val login: String,
        val password: String
)

data class PasswordGenerationRespDto (
        val salt: String,
        val verifier: String
)