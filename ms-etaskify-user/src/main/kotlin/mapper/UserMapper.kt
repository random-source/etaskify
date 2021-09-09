package com.etaskify.ms.user.mapper

import com.etaskify.ms.user.dao.UserEntity
import com.etaskify.ms.user.model.UserDto

fun UserDto.toUserEntity():UserEntity {
    return UserEntity(
            salt = this.salt!!,
            name = this.name!!,
            login = this.login!!,
            email = this.email!!,
            status = this.status,
            surname = this.surname!!,
            verifier = this.verifier!!,
            userType = this.userType,
            organizationId = this.organizationId!!
    )
}

fun UserEntity.toUserDto():UserDto {
    return UserDto(
            id = this.id,
            salt = this.salt,
            name = this.name,
            email = this.email,
            login = this.login,
            status = this.status,
            surname = this.surname,
            verifier = this.verifier,
            userType = this.userType,
            organizationId = this.organizationId
    )
}

fun UserEntity.updateWithDto(userDto: UserDto) {
    salt = userDto.salt ?: salt
    name = userDto.name ?: name
    login = userDto.login ?: login
    status = userDto.status ?: status
    surname = userDto.surname ?: surname
    userType = userDto.userType ?: userType
    verifier = userDto.verifier ?: verifier
    organizationId = userDto.organizationId ?: organizationId
}


