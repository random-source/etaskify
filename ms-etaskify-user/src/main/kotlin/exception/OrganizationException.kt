package com.etaskify.ms.user.exception

class OrganizationException(
        val code: String,
        override val message: String
) : RuntimeException(message)