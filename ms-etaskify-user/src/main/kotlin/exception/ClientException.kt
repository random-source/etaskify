package com.etaskify.ms.user.exception

class ClientException(
        override val message: String,
        val code: String? = null
) : RuntimeException(message)