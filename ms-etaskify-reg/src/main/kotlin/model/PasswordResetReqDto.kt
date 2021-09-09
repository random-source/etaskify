package com.etaskify.ms.reg.model

data class PasswordResetReqDto (
        val salt: String,
        val login: String,
        val verifier: String
)