package com.etaskify.ms.user.service

import com.etaskify.ms.user.dao.UserRepo
import com.etaskify.ms.user.exception.UserException
import com.etaskify.ms.user.mapper.toUserDto
import com.etaskify.ms.user.mapper.updateWithDto
import com.etaskify.ms.user.model.UserDto
import org.springframework.stereotype.Service

@Service
class InternalService(
        val userRepo: UserRepo
) {

    fun getUserByLogin(login: String): UserDto {
        val user = userRepo.findByLogin(login)
                ?: throw UserException("error.userFind.notFound", "User notFound")

        return user.toUserDto()
    }

    fun getUserById(userId: Long): UserDto {
        val user = userRepo.findById(userId)
                .orElseThrow { UserException("error.userFind.userNotFound", "User not found") }

        return user.toUserDto()
    }

    fun patchUser(userId: Long, req: UserDto):UserDto {
        val existedUser = userRepo.findById(userId)
                .orElseThrow { UserException("error.patchUser.userNotFound", "User not found") }

        existedUser.updateWithDto(req)

        userRepo.save(existedUser)

        return existedUser.toUserDto()
    }
}