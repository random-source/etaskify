package com.etaskify.ms.auth.exception

class UserException(
        override val message: String,
        val code: String
) : RuntimeException(message)