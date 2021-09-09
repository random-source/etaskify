package com.etaskify.ms.tasks.controller

import com.etaskify.ms.tasks.model.HeaderKeys
import com.etaskify.ms.tasks.model.TaskCreationDto
import com.etaskify.ms.tasks.service.TaskService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/tasks")
class TasksController(
        private val taskService: TaskService
) {

    @ApiOperation("Create task by organization manager")
    @PostMapping
    fun createTask(
            @RequestHeader(HeaderKeys.HEADER_USER_ID) userId: Long,
            @RequestBody req: TaskCreationDto
    ) = taskService.createTask(userId, req)

    @ApiOperation("Get all task list for organization")
    @GetMapping
    fun getTasksList(
            @RequestHeader(HeaderKeys.HEADER_USER_ID) userId: Long
    ) = taskService.getTasksList(userId)

    @ApiOperation("Get user tasks")
    @GetMapping("/user/tasks")
    fun getUserTasks(
            @RequestHeader(HeaderKeys.HEADER_USER_ID) userId: Long
    ) = taskService.getUserTasks(userId)

    @ApiOperation("Assignee task to user by organization manager")
    @PutMapping("/assignee/task/{taskId}/user/{assignedUserId}")
    fun assigneeTask(
            @RequestHeader(HeaderKeys.HEADER_USER_ID) userId: Long,
            @PathVariable("taskId") taskId: Long,
            @PathVariable("assignedUserId") assignedUserId: Long
    ) = taskService.assigneeTask(userId, assignedUserId, taskId)

}