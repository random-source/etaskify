package com.etaskify.ms.user.exception

class OrganizationCreateException(
        val code: String,
        override val message: String
) : RuntimeException(message)