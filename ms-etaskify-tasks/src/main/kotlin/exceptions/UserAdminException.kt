package com.etaskify.ms.tasks.exceptions

class UserAdminException(
        val code: String,
        override val message: String
) : RuntimeException(message)