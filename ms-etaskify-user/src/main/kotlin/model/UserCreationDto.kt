package com.etaskify.ms.user.model

data class UserCreationDto (
        val name: String? = null,
        val email: String? = null,
        val surname: String? = null,
)