package com.etaskify.ms.reg.controller

import com.etaskify.ms.reg.model.PasswordResetReqDto
import com.etaskify.ms.reg.service.PasswordResetService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sign-up/reset")
class PasswordResetController(
        val passwordResetService: PasswordResetService
) {

    @ApiOperation("Reset password")
    @PostMapping()
    fun resetPassword(@RequestBody req: PasswordResetReqDto) = passwordResetService.resetPassword(req)

}