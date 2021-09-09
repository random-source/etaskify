package com.etaskify.ms.reg.service

import com.etaskify.ms.reg.client.user.UserClient
import com.etaskify.ms.reg.client.user.UserStatus
import com.etaskify.ms.reg.model.PasswordResetReqDto
import org.springframework.stereotype.Service

@Service
class PasswordResetService(
        val userClient: UserClient
) {

    fun resetPassword(req: PasswordResetReqDto) {
       val user = userClient.getUserByLogin(req.login)
               .copy(salt = req.salt, verifier = req.verifier, status = UserStatus.ACTIVE)

        userClient.patchUser(user.id!!, user)

    }

}