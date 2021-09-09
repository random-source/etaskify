package com.etaskify.ms.reg.model

data class RegistrationDto(
        var salt: String,
        var address: String,
        var userName: String,
        var verifier: String,
        var phoneNumber: String,
        var organizationName: String
)
