package com.etaskify.ms.user.exception

class UserAdminException(
        val code: String,
        override val message: String
) : RuntimeException(message)