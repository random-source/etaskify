package com.etaskify.ms.reg.controller

import com.etaskify.ms.reg.model.OrganizationInfoDto
import com.etaskify.ms.reg.model.UserInfoDto
import com.etaskify.ms.reg.service.RegistrationService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sign-up")
class RegistrationController(
        val registrationService: RegistrationService
) {

    @ApiOperation("Register organization")
    @PostMapping("/organization/save")
    fun saveOrganization(@RequestBody organizationInfoDto: OrganizationInfoDto)
                = registrationService.createOrganization(organizationInfoDto)

    @ApiOperation("Register manager")
    @PostMapping("/user/save")
    fun saveUser(@RequestBody userDto: UserInfoDto) = registrationService.createUser(userDto)

}