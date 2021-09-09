package com.etaskify.ms.reg.client.user

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(name = "ms-etaskify-user", url = "\${client.user.url}", configuration = [UserErrorDecoder::class])
interface UserClient {

    @PostMapping("/v1/organization")
    fun createOrganization(@RequestBody organizationDto: OrganizationDto): OrganizationDto

    @PostMapping("/v1/user")
    fun createUser(@RequestBody userDto: UserDto): UserDto

    @GetMapping("/v1/internal/users/login/{login}")
    fun getUserByLogin(@PathVariable login: String): UserDto

    @PutMapping("/v1/internal/users/{userId}")
    fun patchUser(
            @PathVariable userId: Long,
            @RequestBody userDto: UserDto
    )
}

data class OrganizationDto(
        var id: Long? = null,
        var address: String,
        var phoneNumber: String,
        var organizationName: String
)

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
    MANAGER
}

enum class UserStatus {
    ACTIVE
}

