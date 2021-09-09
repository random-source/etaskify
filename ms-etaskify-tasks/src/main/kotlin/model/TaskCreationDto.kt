package com.etaskify.ms.tasks.model

import com.etaskify.ms.tasks.enums.TaskStatus
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime
import java.util.*

data class TaskCreationDto (
        val id: Long? = null,
        val title: String? = null,
        val status: TaskStatus? = null,
        val description: String? = null,
        val deadline: Date? = null
)