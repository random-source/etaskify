package com.etaskify.ms.auth.model

import io.swagger.annotations.ApiModelProperty

data class SrpFirstStepReqDto(
        val login: String
)

data class SrpFirstStepRespDto(
        val srpB: String,
        val srpS: String
)

data class SrpSecondStepReqDto(
        val login: String,
        val srpA: String,
        val srpM1: String
)

data class SrpSecondStepResDto(
        val srpM2: String,
        val accessToken: String,
        val refreshToken: String
)