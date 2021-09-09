package com.etaskify.ms.auth.client.user

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.io.Serializable

@FeignClient(name = "ms-etaskify-user", url = "\${client.user.url}", configuration = [UserErrorDecoder::class])
interface UserClient {

    @GetMapping("/v1/internal/users/login/{login}")
    fun getUserByLogin(@PathVariable login: String): UserDto


}

data class UserDto(
        var id: Long? = null,
        var email: String,
        var salt: String,
        var login: String,
        var status: UserStatus? = UserStatus.ACTIVE,
        var username: String,
        var verifier: String,
        var userType: UserType = UserType.MANAGER,
        var organizationId: Long,
): Serializable

enum class UserType {
    MANAGER
}

enum class UserStatus {
    ACTIVE, RESET_PASSWORD
}



