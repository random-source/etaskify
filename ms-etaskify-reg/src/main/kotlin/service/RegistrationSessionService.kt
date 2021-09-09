package com.etaskify.ms.reg.service

import com.etaskify.ms.reg.exceptions.CacheException
import com.etaskify.ms.reg.model.OrganizationInfoDto
import org.redisson.Redisson
import org.redisson.api.RBucket
import org.redisson.api.RedissonClient
import org.redisson.codec.SerializationCodec
import org.redisson.config.Config
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class RegistrationSessionService(private val redissonClient: RedissonClient) {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
        private const val CACHE_PREFIX = "TASKIFY_REGISTRATION_SESSION"
        private const val EXPIRATION_TIME = 5L
    }

    fun getSessionData(sessionKey: String): OrganizationCache {
        val bucket = getBucket(sessionKey)
        if (!bucket.isExists) {
            log.error("Bucket not exists")
            throw CacheException(
                    code = "error.registration.sessionExpired",
                    message = "Registration session expired start from otp"
            )
        }
        return bucket.get()
    }

    fun addSessionData(organizationInfoDto: OrganizationInfoDto): String {
        val sessionKey = UUID.randomUUID().toString()
        val bucket = getBucket(sessionKey)
        log.debug("Adding data to bucket")
        bucket.set(OrganizationCache(
                organizationInfoDto.organizationName!!,
                organizationInfoDto.address!!,
                organizationInfoDto.phoneNumber!!,
        ))
        bucket.expire(EXPIRATION_TIME, TimeUnit.MINUTES)

        return sessionKey
    }

    fun deleteRegistrationSession(sessionKey: String) {
        val bucket = getBucket(sessionKey)
        log.debug("Deleting session")
        bucket.delete()
    }

    private fun getBucket(key: String): RBucket<OrganizationCache> {
        val bucketKey = "$CACHE_PREFIX:$key"
        log.debug("Getting bucket: $bucketKey")
        return redissonClient.getBucket(bucketKey)
    }

    data class OrganizationCache(var name: String, var address: String, val phoneNumber: String) : Serializable

}
