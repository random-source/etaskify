package com.etaskify.ms.auth.exception

class AuthException(
        override val message: String,
        val code: String
) : RuntimeException(message)