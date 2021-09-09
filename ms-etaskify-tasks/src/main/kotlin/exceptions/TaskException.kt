package com.etaskify.ms.tasks.exceptions

class TaskException(
        val code: String,
        override val message: String
) : RuntimeException(message)