package com.etaskify.ms.tasks.mapper

import com.etaskify.ms.tasks.dao.TaskEntity
import com.etaskify.ms.tasks.model.TaskCreationDto
import com.etaskify.ms.tasks.model.TasksListRespDto

fun TaskCreationDto.toTaskEntity():TaskEntity {
    return TaskEntity(
            title = this.title!!,
            deadline = this.deadline!!,
            description = this.description!!
    )
}


fun TaskEntity.toTasksListDto(): TasksListRespDto{
    return TasksListRespDto(
            id = this.id!!,
            title = this.title,
            status = this.status,
            deadline = this.deadline!!,
            description = this.description,
            createdUserId = this.createdUserId,
            createdUserName = this.createdUserName!!,
            createdUserSurname = this.createdUserSurname!!,
            assignedUserId = this.assignedUserId,
            assignedUserName = this.assignedUserName,
            assignedUserSurname = this.assignedUserSurname
    )
}

fun List<TaskEntity>.toDtoList(): List<TasksListRespDto> {
    return this.map { it.toTasksListDto() }
}