package com.etaskify.ms.tasks.model

import com.etaskify.ms.tasks.enums.TaskStatus
import java.time.LocalDateTime
import java.util.*


data class TasksListRespDto (
        val id: Long,
        val title: String,
        val status: TaskStatus,
        val description: String,
        val deadline: Date,
        val createdUserId: Long,
        val createdUserName: String,
        val createdUserSurname: String,
        val assignedUserId: Long,
        val assignedUserName: String? = null,
        val assignedUserSurname: String? = null,
)