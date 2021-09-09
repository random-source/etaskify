package com.etaskify.ms.user.model

import com.etaskify.ms.user.enums.UserStatus
import com.etaskify.ms.user.enums.UserType

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