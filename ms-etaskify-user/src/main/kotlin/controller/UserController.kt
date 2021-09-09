package com.etaskify.ms.user.controller

import com.etaskify.ms.user.model.HeaderKeys
import com.etaskify.ms.user.model.UserCreationDto
import com.etaskify.ms.user.model.UserDto
import com.etaskify.ms.user.service.UserService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user")
class UserController(
        val userService: UserService
) {

    @ApiOperation("Create manager user for organization")
    @PostMapping
    fun createUser(@RequestBody req: UserDto) = userService.createUser(req)

    @ApiOperation("Create user by manager for organization")
    @PostMapping("/admin/create")
    fun createUserByAdmin(
            @RequestHeader(HeaderKeys.HEADER_USER_ID) userId: Long,
            @RequestBody req: UserCreationDto
    ) = userService.createUserByAdmin(userId, req)

}