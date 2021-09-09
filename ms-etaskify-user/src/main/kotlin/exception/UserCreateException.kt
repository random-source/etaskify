package com.etaskify.ms.user.exception

class UserCreateException(
        val code: String,
        override val message: String
) : RuntimeException(message)