package com.etaskify.ms.auth.controller

import com.etaskify.ms.auth.model.PasswordGenerationReqDto
import com.etaskify.ms.auth.service.InternalService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api("Authentication API")
@RequestMapping("/v1/internal/auth")
class InternalController(
        val internalService: InternalService
) {

    @ApiOperation("Password generation")
    @PostMapping("/password")
    fun generatePassword(@RequestBody req: PasswordGenerationReqDto) = internalService.generateDefaultPassword(req)

}