package com.etaskify.ms.reg.service

import com.etaskify.ms.reg.client.user.*
import com.etaskify.ms.reg.model.OrganizationInfoDto
import com.etaskify.ms.reg.model.OrganizationInfoResponseDto
import com.etaskify.ms.reg.model.UserInfoDto
import com.etaskify.ms.reg.model.UserResponseDto
import com.etaskify.ms.reg.util.RegistrationUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class RegistrationService(
        val userClient: UserClient,
        val registrationUtil: RegistrationUtil,
        val registrationSessionService: RegistrationSessionService
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    fun createOrganization(req: OrganizationInfoDto): OrganizationInfoResponseDto{
        registrationUtil.validateOrganizationInfo(req)

        val sessionKey = registrationSessionService.addSessionData(req)

        return OrganizationInfoResponseDto(UUID.randomUUID().toString(), sessionKey)
    }

    fun createUser(req: UserInfoDto): UserResponseDto{
        registrationUtil.validateUserInfo(req)

        val organization = registrationSessionService.getSessionData(req.sessionKey)

        val org = OrganizationDto(
                address = organization.address,
                phoneNumber = organization.phoneNumber,
                organizationName = organization.name
        )

        val createdOrganization = userClient.createOrganization(org)

        val test = UserDto(
                salt = req.salt!!,
                name = req.name!!,
                email = req.email!!,
                login = req.login!!,
                surname = req.surname,
                verifier = req.verifier!!,
                status = UserStatus.ACTIVE,
                userType = UserType.MANAGER,
                organizationId = createdOrganization.id!!
        )

        val createdUser = userClient.createUser(test)

        registrationSessionService.deleteRegistrationSession(req.sessionKey)

        return UserResponseDto(
                login = createdUser.login!!,
                email = createdUser.email!!,
                name = createdUser.name!!
        )

    }

}