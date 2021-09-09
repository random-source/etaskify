package com.etaskify.ms.user.controller

import com.etaskify.ms.user.model.OrganizationDto
import com.etaskify.ms.user.service.OrganizationService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/organization")
class OrganizationController(
        val organizationService: OrganizationService
) {

    @ApiOperation("Create organization profile")
    @PostMapping
    fun createOrganization(@RequestBody organizationDto: OrganizationDto)
            = organizationService.createOrganization(organizationDto)

}