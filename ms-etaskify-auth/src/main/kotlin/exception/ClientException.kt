package com.etaskify.ms.auth.exception

class ClientException(
        override val message: String,
        val code: String? = null
) : RuntimeException(message)