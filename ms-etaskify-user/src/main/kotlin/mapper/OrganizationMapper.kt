package com.etaskify.ms.user.mapper

import com.etaskify.ms.user.dao.OrganizationEntity
import com.etaskify.ms.user.model.OrganizationDto


fun OrganizationDto.toOrganizationEntity(): OrganizationEntity{
    return OrganizationEntity(
            address = this.address,
            phoneNumber = this.phoneNumber,
            organizationName = this.organizationName
    )
}

fun OrganizationEntity.toOrganizationDto(): OrganizationDto{
    return OrganizationDto(
            id = this.id,
            address = this.address,
            phoneNumber = this.phoneNumber,
            organizationName = this.organizationName
    )
}