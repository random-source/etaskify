package com.etaskify.ms.tasks.exceptions

class UserException(
        val code: String,
        override val message: String
) : RuntimeException(message)