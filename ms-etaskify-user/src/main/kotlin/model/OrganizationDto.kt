package com.etaskify.ms.user.model

data class OrganizationDto(
        var id: Long? = null,
        var address: String,
        var phoneNumber: String,
        var organizationName: String
)
