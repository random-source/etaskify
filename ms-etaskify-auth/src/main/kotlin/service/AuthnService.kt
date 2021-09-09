package com.etaskify.ms.auth.service

import com.etaskify.ms.auth.client.user.UserClient
import com.etaskify.ms.auth.client.user.UserStatus
import com.etaskify.ms.auth.crypto.generateRandomSalt
import com.etaskify.ms.auth.crypto.generateSRP6ServerSession
import com.etaskify.ms.auth.exception.AuthException
import com.etaskify.ms.auth.model.SrpFirstStepReqDto
import com.etaskify.ms.auth.model.SrpFirstStepRespDto
import com.etaskify.ms.auth.model.SrpSecondStepReqDto
import com.etaskify.ms.auth.model.SrpSecondStepResDto
import com.nimbusds.srp6.BigIntegerUtils
import com.nimbusds.srp6.SRP6Exception
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class AuthnService(
        private val authnCacheService: AuthnCacheService,
        private val tokenService: TokenService,
        private val userClient: UserClient
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    fun srpAuthFirstStep(req: SrpFirstStepReqDto): SrpFirstStepRespDto {
        val srpSession = generateSRP6ServerSession()

        // Init variables with fake data
        var srp1B: BigInteger
        var salt: BigInteger

        val srpCacheData = SRPCacheData(srpSession = srpSession)

        try {
            val user = userClient.getUserByLogin(req.login)

            log.debug("Srp step first for userId ${user.id}")

            srpCacheData.user = user

            // Retrieve user verifier 'v' + salt 's' from database
            val v = BigIntegerUtils.fromHex(user.verifier)
            salt = BigIntegerUtils.fromHex(user.salt)

            // Compute the public server value 'B'
            srp1B = srpSession.step1(req.login, salt, v)
        } catch (ex: AuthException) {
            log.warn("Failed to do real SRP1", ex)
            // Use fake srp1 step, in order to give error only on thee second step
            salt = generateRandomSalt()
            srp1B = srpSession.mockStep1(req.login, salt, BigInteger.ZERO)
        }

        authnCacheService.addSessionData(req.login, srpCacheData)

        return SrpFirstStepRespDto(
                srpB = BigIntegerUtils.toHex(srp1B),
                srpS = BigIntegerUtils.toHex(salt)
        )
    }

    fun srpAuthStep2(req: SrpSecondStepReqDto): SrpSecondStepResDto {
        // Verify cache
        val cacheData = authnCacheService.getSessionDataByLogin(req.login)

        // Verify srp 2
        val a = BigIntegerUtils.fromHex(req.srpA)
        val m1 = BigIntegerUtils.fromHex(req.srpM1)

        val srpSession = cacheData.srpSession

        val spr2M2 = try {
            srpSession.step2(a, m1)
        } catch (ex: SRP6Exception) {
            log.debug("Invalid password")
            throw AuthException("error.auth.invalidCredentials", "Invalid credentials")
        }

        if(cacheData.user!!.status == UserStatus.RESET_PASSWORD){
            log.debug("User rest password")
            throw AuthException("error.auth.resetPassword", "Reset password")
        }

        // Generate tokens
        val tokens = tokenService.generateTokens(cacheData.user!!.id!!)

        // Clear cache
        authnCacheService.deleteSessionDataByLogin(req.login)

        return SrpSecondStepResDto(
                srpM2 = BigIntegerUtils.toHex(spr2M2),
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken
        )
    }


}