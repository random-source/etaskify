package com.etaskify.ms.auth.model

import java.io.Serializable
import java.time.LocalDateTime

data class AccessTokenClaimsSet(
        val userId: Long,
        private val expirationTimeInSeconds: Long,
        val createdAt: LocalDateTime = LocalDateTime.now()
) : Serializable {
    val expiresAt = createdAt.plusSeconds(expirationTimeInSeconds)
}

data class RefreshTokenClaimsSet(
        val userId: Long,
        val count: Int,
        private val expirationTimeInSeconds: Long
) : Serializable {
    val expiresAt = LocalDateTime.now().plusSeconds(expirationTimeInSeconds)
}