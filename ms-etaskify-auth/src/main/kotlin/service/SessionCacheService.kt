package com.etaskify.ms.auth.service

import com.etaskify.ms.auth.exception.AuthException
import com.etaskify.ms.auth.model.AccessTokenClaimsSet
import com.google.gson.Gson
import org.redisson.api.RBucket
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.concurrent.TimeUnit

@Component
class SessionCacheService(
        private val redissonClient: RedissonClient,
        private val gson: Gson
) {

    companion object {
        private const val SESSION_CACHE_PREFIX = "TASKIFY_AUTH_SESSION"
    }

    fun putCacheData(userId: Long, data: SessionCacheData, expirationSecs: Long) {
        val bucket = getCacheBucket(userId)
        bucket.set(gson.toJson(data))
        bucket.expire(expirationSecs, TimeUnit.SECONDS)
    }

    private fun getCacheBucket(userId: Long): RBucket<String> {
        val bucketKey = "$SESSION_CACHE_PREFIX:$userId"
        return redissonClient.getBucket(bucketKey)
    }

    fun getCacheData(userId: Long): SessionCacheData {
        val bucket = getCacheBucket(userId)
        if (!bucket.isExists) {
            throw AuthException("error.session.expired", "Session is expired")
        }
        return gson.fromJson(bucket.get(), SessionCacheData::class.java)
    }
}

data class SessionCacheData(
        val publicKey: String,
        val accessTokenClaimsSet: AccessTokenClaimsSet
) : Serializable