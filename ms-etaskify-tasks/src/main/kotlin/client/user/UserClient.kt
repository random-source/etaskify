package com.etaskify.ms.tasks.client.user

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(name = "ms-etaskify-user", url = "\${client.user.url}", configuration = [UserErrorDecoder::class])
interface UserClient {
    @GetMapping("/v1/internal/users/id/{userId}")
    fun getUserById(@PathVariable userId: Long): UserDto
}

data class UserDto(
        var id: Long? = null,
        var name: String? = null,
        var email: String? = null,
        var salt: String? = null,
        var login: String? = null,
        var status: UserStatus?,
        var surname: String? = null,
        var verifier: String? = null,
        var userType: UserType?,
        var organizationId: Long? = null
)

enum class UserType {
    MANAGER, EMPLOYEE
}

enum class UserStatus {
    ACTIVE
}

