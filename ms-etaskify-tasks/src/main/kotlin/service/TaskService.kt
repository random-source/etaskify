package com.etaskify.ms.tasks.service

import com.etaskify.ms.tasks.client.user.UserClient
import com.etaskify.ms.tasks.client.user.UserStatus
import com.etaskify.ms.tasks.client.user.UserType
import com.etaskify.ms.tasks.dao.TaskRepo
import com.etaskify.ms.tasks.exceptions.UserAdminException
import com.etaskify.ms.tasks.exceptions.TaskCreationException
import com.etaskify.ms.tasks.exceptions.TaskException
import com.etaskify.ms.tasks.exceptions.UserException
import com.etaskify.ms.tasks.mapper.toDtoList
import com.etaskify.ms.tasks.mapper.toTaskEntity
import com.etaskify.ms.tasks.model.TaskCreationDto
import com.etaskify.ms.tasks.model.TasksListRespDto
import org.springframework.stereotype.Service

@Service
class TaskService(
        private val taskRepo: TaskRepo,
        private val userClient: UserClient,
        private val notifyService: NotifyService
) {

    fun createTask(userId:Long, req: TaskCreationDto) {
        if(req.title.isNullOrBlank() || req.description.isNullOrBlank() || req.deadline == null){
            throw TaskCreationException("error.taskCreate.EmptyTaskParam", "Task empty value")
        }

        var task = req.toTaskEntity()

        val user = userClient.getUserById(userId)

        checkIfUserManager(user.userType!!)

        task = task
                .copy(
                        createdUserId = user.id!!,
                        createdUserName = user.name!!,
                        createdUserSurname = user.surname!!,
                        organizationId = user.organizationId!!
                )

        taskRepo.save(task)
    }

    fun getTasksList(userId: Long): List<TasksListRespDto> {
        val user = userClient.getUserById(userId)

        val tasks = taskRepo.findAllByOrganizationId(user.organizationId!!)

        return tasks.toDtoList()
    }

    fun getUserTasks(userId: Long): List<TasksListRespDto> {
        val tasks = taskRepo.findAllByAssignedUserId(userId)

        return tasks.toDtoList()
    }

    fun assigneeTask(userId: Long, assignedUserId: Long, taskId: Long){
        val user = userClient.getUserById(userId)

        checkIfUserManager(user.userType!!)

        val assignedUser = userClient.getUserById(assignedUserId)

        if(assignedUser.status != UserStatus.ACTIVE){
            throw UserException("error.assigneeTask.userNotActive", "User is not active")
        }

        val task = taskRepo.findById(taskId).orElseThrow{ TaskException("error.assigneeTask.notFound", "Task not found")}

        task.assignedUserId = assignedUser.id!!
        task.assignedUserName = assignedUser.name
        task.assignedUserSurname = assignedUser.surname

        taskRepo.save(task)

        notifyService.notifyUser(user.name!!, user.surname!!, task.title)
    }


    private fun checkIfUserManager(userType: UserType){
        if(userType != UserType.MANAGER){
            throw UserAdminException("error.createUser.notAdmin", "User is not admin")
        }
    }

}
