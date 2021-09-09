package com.etaskify.ms.auth.model

data class TokensDto(
        val accessToken: String,
        val refreshToken: String
)