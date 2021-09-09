package com.etaskify.ms.auth.model

import io.swagger.annotations.ApiModelProperty

data class PasswordGenerationRespDto (
        val salt: String,
        val verifier: String
)