package com.etaskify.ms.auth.service

import com.etaskify.ms.auth.crypto.generateSaltAndVerifierForUser
import com.etaskify.ms.auth.model.PasswordGenerationReqDto
import com.etaskify.ms.auth.model.PasswordGenerationRespDto
import org.springframework.stereotype.Service

@Service
class InternalService {

    fun generateDefaultPassword(req: PasswordGenerationReqDto): PasswordGenerationRespDto {
        val result = generateSaltAndVerifierForUser(req.login, req.password)
        return PasswordGenerationRespDto(
                salt = result.first, verifier = result.second
        )
    }

}