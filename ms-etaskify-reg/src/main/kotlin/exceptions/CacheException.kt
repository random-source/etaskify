package com.etaskify.ms.reg.exceptions

class CacheException(
        val code: String,
        override val message: String
) : RuntimeException(message)