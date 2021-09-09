package com.etaskify.ms.reg.model

data class UserInfoDto (
        val name: String? = null,
        val salt: String? = null,
        val email: String? = null,
        val login: String? = null,
        val verifier: String? = null,
        val surname: String? = null,
        val sessionKey: String
)