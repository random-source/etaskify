package com.etaskify.ms.user.exception

class UserException(
        val code: String,
        override val message: String
) : RuntimeException(message)