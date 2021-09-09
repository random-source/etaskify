package com.etaskify.ms.tasks.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RestErrorResponse(
        val code: String? = null,
        val message: String,
        val httpCode: Int? = null,
        val isError: Boolean? = null
)