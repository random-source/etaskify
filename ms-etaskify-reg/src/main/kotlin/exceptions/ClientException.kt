package com.etaskify.ms.reg.exceptions

class ClientException(
        override val message: String,
        val code: String? = null
) : RuntimeException(message)