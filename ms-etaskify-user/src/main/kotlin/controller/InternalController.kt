package com.etaskify.ms.user.controller

import com.etaskify.ms.user.model.UserDto
import com.etaskify.ms.user.service.InternalService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/internal")
class InternalController(
    val internalService: InternalService
) {

    @ApiOperation("Get user by login")
    @GetMapping("/users/login/{login}")
    fun getUserByLogin(@PathVariable login: String) = internalService.getUserByLogin(login)

    @ApiOperation("Get user by id")
    @GetMapping("/users/id/{userId}")
    fun getUserById(@PathVariable userId: Long) = internalService.getUserById(userId)

    @ApiOperation("Patch user")
    @PutMapping("/users/{userId}")
    fun patchUser(
            @PathVariable userId: Long,
            @RequestBody userDto: UserDto
    ) = internalService.patchUser(userId, userDto)
}