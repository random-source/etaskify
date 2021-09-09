package com.etaskify.ms.tasks.exceptions

class ClientException(
        override val message: String,
        val code: String? = null
) : RuntimeException(message)