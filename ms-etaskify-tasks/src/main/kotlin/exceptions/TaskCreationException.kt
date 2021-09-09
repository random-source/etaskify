package com.etaskify.ms.tasks.exceptions

class TaskCreationException(
        val code: String,
        override val message: String
) : RuntimeException(message)