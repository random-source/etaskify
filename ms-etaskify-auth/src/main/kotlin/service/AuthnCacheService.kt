package com.etaskify.ms.auth.service

import com.etaskify.ms.auth.client.user.UserDto
import com.etaskify.ms.auth.exception.CacheException
import com.nimbusds.srp6.SRP6ServerSession
import org.redisson.api.RBucket
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.concurrent.TimeUnit

@Service
class AuthnCacheService(
        private val redissonClient: RedissonClient
) {

    companion object {
        private val log = LoggerFactory.getLogger(AuthnCacheService::class.java)
        private const val CACHE_PREFIX = "TASKIFY_SRP_SESSION"
        private const val EXPIRATION_TIME = 3L
    }

    fun getSessionDataByLogin(login: String): SRPCacheData {
        val bucket = getBucket(login)
        if (!bucket.isExists) {
            log.error("Bucket not exists")
            throw CacheException("auth.expired-authCache", "Pre auth cache is expired")
        }
        return bucket.get()
    }

    fun addSessionData(login: String, data: SRPCacheData) {
        val bucket = getBucket(login)
        log.debug("Adding data to bucket")
        bucket.set(data)
        bucket.expire(EXPIRATION_TIME, TimeUnit.MINUTES)
    }

    fun deleteSessionDataByLogin(login: String) {
        val bucket = getBucket(login)
        log.debug("Deleting bucket")
        bucket.delete()
    }

    private fun getBucket(key: String): RBucket<SRPCacheData> {
        val bucketKey = "$CACHE_PREFIX:$key"
        log.debug("Getting bucket: $bucketKey")
        return redissonClient.getBucket(bucketKey)
    }
}

data class SRPCacheData(
        val srpSession: SRP6ServerSession,
        var user: UserDto? = null
) : Serializable