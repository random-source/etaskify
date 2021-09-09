package com.etaskify.ms.auth.exception

class CacheException(
        override val message: String,
        val code: String
) : RuntimeException(message)