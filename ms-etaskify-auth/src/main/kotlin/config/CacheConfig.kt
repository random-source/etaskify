package com.etaskify.ms.auth.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.SerializationCodec
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class CacheConfig(
        @Value("\${redis.server.urls}") val redisServers: Array<String>
) {

    companion object {
        private const val SCAN_INTERVAL = 2000
        private const val CONNECTION_POOL_SIZE = 4
        private const val CONNECTION_MINIMUM_IDLE_SIZE = 2
    }

    @Bean
    @Profile("default")
    fun redissonClusterClient(): RedissonClient {
        val config = Config()
        config.setCodec(SerializationCodec())
                .useClusterServers()
                .setScanInterval(SCAN_INTERVAL)
                .setMasterConnectionPoolSize(CONNECTION_POOL_SIZE)
                .setMasterConnectionMinimumIdleSize(CONNECTION_MINIMUM_IDLE_SIZE)
                .setSlaveConnectionPoolSize(CONNECTION_POOL_SIZE)
                .setSlaveConnectionMinimumIdleSize(CONNECTION_MINIMUM_IDLE_SIZE)
                .addNodeAddress(*redisServers)
        return Redisson.create(config)
    }

    @Bean
    @Profile("dev", "local")
    fun redissonSingleClient(): RedissonClient {
        val config = Config()
        config.setCodec(SerializationCodec())
                .useSingleServer().address = redisServers[0]
        return Redisson.create(config)
    }
}