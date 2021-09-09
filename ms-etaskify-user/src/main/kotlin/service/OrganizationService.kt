package com.etaskify.ms.user.service

import com.etaskify.ms.user.dao.OrganizationRepo
import com.etaskify.ms.user.exception.OrganizationCreateException
import com.etaskify.ms.user.mapper.toOrganizationDto
import com.etaskify.ms.user.mapper.toOrganizationEntity
import com.etaskify.ms.user.model.OrganizationDto
import org.springframework.stereotype.Service

@Service
class OrganizationService(
        val organizationRepo: OrganizationRepo
) {

    fun createOrganization(req: OrganizationDto): OrganizationDto{

        val exists = organizationRepo.existsByPhoneNumber(req.phoneNumber)

        if(exists) throw OrganizationCreateException("error.organizationCreate.exists", "Organization already exists")

        val organization = req.toOrganizationEntity()

        val savedOrganization = organizationRepo.save(organization)

        return savedOrganization.toOrganizationDto()
    }

}