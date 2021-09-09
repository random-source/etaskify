package com.etaskify.ms.user.service

import com.etaskify.ms.user.client.auth.AuthClient
import com.etaskify.ms.user.client.auth.PasswordGenerationReqDto
import com.etaskify.ms.user.dao.OrganizationRepo
import com.etaskify.ms.user.dao.UserEntity
import com.etaskify.ms.user.dao.UserRepo
import com.etaskify.ms.user.enums.UserStatus
import com.etaskify.ms.user.enums.UserType
import com.etaskify.ms.user.exception.OrganizationCreateException
import com.etaskify.ms.user.exception.UserAdminException
import com.etaskify.ms.user.exception.UserCreateException
import com.etaskify.ms.user.exception.UserException
import com.etaskify.ms.user.mapper.toUserDto
import com.etaskify.ms.user.mapper.toUserEntity
import com.etaskify.ms.user.model.UserCreationDto
import com.etaskify.ms.user.model.UserDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
        @Value("\${user.password}")
        private val password: String,
        private val userRepo: UserRepo,
        private val authClient: AuthClient,
        private val organizationRepo: OrganizationRepo
) {

    fun createUser(req: UserDto): UserDto {
        val exists = userRepo.existsByLogin(req.login!!)

        if (exists) throw UserCreateException("error.userCreate.exists", "User already exists")

        val organizationExists = organizationRepo.existsById(req.organizationId!!)

        if (!organizationExists) throw UserCreateException("error.userCreate.notExists", "Organization not exists")

        val user = req.toUserEntity()

        val savedUser = userRepo.save(user)

        return savedUser.toUserDto()
    }

    fun createUserByAdmin(userId: Long, req: UserCreationDto): UserDto {
        val admin = userRepo.findById(userId)
                 .orElseThrow { UserException("error.userNotFound", "User not found") }

        if(admin.userType != UserType.MANAGER){
            throw UserAdminException("error.createUser.notAdmin", "User is not admin")
        }

        if(req.name.isNullOrBlank() || req.surname.isNullOrBlank() || req.email.isNullOrBlank()){
            throw UserAdminException("error.createUser.emptyUserParam", "User empty value")
        }

        val exists = userRepo.existsByEmail(req.email)

        if (exists) throw UserCreateException("error.createUser.exists", "User already exists")

        val login = UUID.randomUUID().toString()

        val resp = authClient.generatePassword(PasswordGenerationReqDto(
                login = login,
                password = password
        ))

        val savedUser = userRepo.save(UserEntity(
                login = login,
                name = req.name,
                salt = resp.salt,
                email = req.email,
                surname = req.surname,
                verifier = resp.verifier,
                status = UserStatus.RESET_PASSWORD,
                userType = UserType.EMPLOYEE,
                organizationId = admin.organizationId
            )
        )

        return savedUser.toUserDto()

    }
}