package com.etaskify.ms.reg.util

import com.etaskify.ms.reg.exceptions.RegistrationException
import com.etaskify.ms.reg.model.OrganizationInfoDto
import com.etaskify.ms.reg.model.UserInfoDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class RegistrationUtil {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    fun validateOrganizationInfo(req: OrganizationInfoDto){
        if(req.address.isNullOrBlank()){
            val code = "error.createOrganization.emptyAddress"
            val message = "Empty organization address"
            log.debug("ActionLog.validateOrganizationInfo.error: $message")
            buildException(code, message)
        }

        if(req.phoneNumber.isNullOrBlank()){
            val code = "error.createOrganization.emptyPhoneNumber"
            val message = "Empty organization phone number"
            log.debug("ActionLog.validateOrganizationInfo.error: $message")
            buildException(code, message)
        }

        if(req.organizationName.isNullOrBlank()){
            val code = "error.createOrganization.emptyOrganizationName"
            val message = "Empty organization name"
            log.debug("ActionLog.validateOrganizationInfo.error: $message")
            buildException(code, message)
        }

    }

    fun validateUserInfo(req: UserInfoDto){
        if(req.name.isNullOrBlank()){
            val code = "error.createUser.emptyName"
            val message = "Empty name"
            log.debug("ActionLog.validateUserInfo.error: $message")
            buildException(code, message)
        }

        if(req.surname.isNullOrBlank()){
            val code = "error.createUser.emptySurname"
            val message = "Empty surname"
            log.debug("ActionLog.validateUserInfo.error: $message")
            buildException(code, message)
        }

        if(req.email.isNullOrBlank()){
            val code = "error.createUser.emptyEmail"
            val message = "Empty email"
            log.debug("ActionLog.validateUserInfo.error: $message")
            buildException(code, message)
        }

        if(req.salt.isNullOrBlank() || req.verifier.isNullOrBlank()){
            val code = "error.createUser.invalidParam"
            val message = "Invalid param"
            log.debug("ActionLog.validateUserInfo.error: $message")
            buildException(code, message)
        }

    }

    private fun buildException(code: String, message: String){
        throw RegistrationException(
                code,
                message
        )
    }

}