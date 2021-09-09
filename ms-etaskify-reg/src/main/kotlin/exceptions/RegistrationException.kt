package com.etaskify.ms.reg.exceptions

class RegistrationException(
        val code: String,
        override val message: String
) : RuntimeException(message)