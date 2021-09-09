package com.etaskify.ms.auth.service

import com.etaskify.ms.auth.exception.AuthException
import com.etaskify.ms.auth.model.AccessTokenClaimsSet
import com.etaskify.ms.auth.model.RefreshTokenClaimsSet
import com.etaskify.ms.auth.model.TokensDto
import com.etaskify.ms.auth.service.JwtService
import com.etaskify.ms.auth.service.SessionCacheData
import com.etaskify.ms.auth.service.SessionCacheService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import java.security.KeyPair
import java.time.LocalDateTime

@Service
class TokenService(
        private val jwtService: JwtService,

        private val sessionCacheService: SessionCacheService,

        @Value("\${jwt.accessToken.expirationTime}")
        private val accessTokenExpirationTimeSecs: Long,

        @Value("\${jwt.refreshToken.expirationTime}")
        val refreshTokenExpirationTimeSecs: Long,

        @Value("\${jwt.refreshToken.expirationCount}")
        val refreshTokenExpirationCount: Int
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    fun generateTokens(
            userId: Long,
            count: Int = refreshTokenExpirationCount
    ): TokensDto {
        val accessTokenClaimsSet = AccessTokenClaimsSet(userId, accessTokenExpirationTimeSecs)
        val refreshTokenClaimsSet = RefreshTokenClaimsSet(userId, count, refreshTokenExpirationTimeSecs)

        val keyPair: KeyPair = jwtService.generateKeyPair()

        val accessToken = jwtService.generateToken(accessTokenClaimsSet, keyPair.private)
        val refreshToken = jwtService.generateToken(refreshTokenClaimsSet, keyPair.private)

        val sessionData = SessionCacheData(
                Base64Utils.encodeToString(keyPair.public.encoded),
                accessTokenClaimsSet
        )

        sessionCacheService.putCacheData(
                userId, sessionData,
                refreshTokenExpirationTimeSecs
        )

        log.debug("generateTokens -- saved session cache data")

        return TokensDto(accessToken, refreshToken)
    }

    fun refreshTokens(refreshToken: String): TokensDto {
        val claimsSet = jwtService.getClaimsFromRefreshToken(refreshToken)

        val cacheData = sessionCacheService.getCacheData(claimsSet.userId)

        jwtService.verifyToken(refreshToken, cacheData.publicKey)

        // Check refresh token expiration time
        if (claimsSet.expiresAt.isBefore(LocalDateTime.now())) {
            log.debug("Refresh token time has been expired")
            throw AuthException("error.refreshToken.expired", "Refresh token has been expired")
        }

        // Check refresh token expiration count
        if (claimsSet.count <= 0) {
            throw AuthException("error.refreshToken.expired", "Refresh token has been expired")
        }

        return generateTokens(claimsSet.userId, claimsSet.count - 1)
    }

    fun verifyAccessToken(accessToken: String?): Long {
        if (accessToken == null) {
            log.warn("Access token is missing")
            throw AuthException("error.validateAccessToken.expired", "Access token is missing")
        }

        val claimsSet = jwtService.getClaimsFromAccessToken(accessToken)
        log.debug("Verifying token for user: ${claimsSet.userId}")
        validateExpiration(claimsSet)
        val cacheData = sessionCacheService.getCacheData(claimsSet.userId)

        jwtService.verifyToken(accessToken, cacheData.publicKey)

        return claimsSet.userId
    }

    private fun validateExpiration(accessTokenClaimsSet: AccessTokenClaimsSet) {
        if (accessTokenClaimsSet.expiresAt.isBefore(LocalDateTime.now())) {
            throw AuthException("error.validateAccessToken.expired", "Access token expired")
        }
    }
}