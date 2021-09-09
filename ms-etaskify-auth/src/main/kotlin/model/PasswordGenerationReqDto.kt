package com.etaskify.ms.auth.model

import io.swagger.annotations.ApiModelProperty

data class PasswordGenerationReqDto (
        @ApiModelProperty(value = "Login key for get user", example = "b5d99774-115b-11ec-82a8-0242ac130003")
        val login: String,

        @ApiModelProperty(value = "User password")
        val password: String
)