package com.etaskify.ms.auth.controller

import com.etaskify.ms.auth.model.*
import com.etaskify.ms.auth.service.AuthnService
import com.etaskify.ms.auth.service.TokenService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Api("Authentication API")
@RequestMapping("/v1/auth")
class AuthnController(
        private val authnService: AuthnService,
        private val tokenService: TokenService
        ) {

    companion object {
        private const val HEADER_REFRESH_TOKEN = "refresh-token"
    }

    @ApiOperation("SRP step 1")
    @PostMapping("/srp/step1")
    fun srpAuthFirstStep(@RequestBody req: SrpFirstStepReqDto): ResponseEntity<SrpFirstStepRespDto> {
        val res = authnService.srpAuthFirstStep(req)
        return ResponseEntity.ok(res)
    }

    @ApiOperation("SRP step 2")
    @PostMapping("/srp/step2")
    fun srpAuthStep2(@RequestBody req: SrpSecondStepReqDto): ResponseEntity<SrpSecondStepResDto> {
        val res = authnService.srpAuthStep2(req)
        return ResponseEntity.ok(res)
    }

    @GetMapping("/token/verify")
    fun verifyToken(
            @RequestHeader("dp-access-token", required = false) accessToken: String?
    ): ResponseEntity<String> {
        val verifiedUserId = tokenService.verifyAccessToken(accessToken)
        return ResponseEntity.ok()
                .header("taskify-auth-user-id", verifiedUserId.toString())
                .build()
    }

    @ApiOperation("Refresh tokens")
    @PostMapping("/token/refresh")
    fun refreshTokens(@RequestHeader(HEADER_REFRESH_TOKEN) refreshToken: String): ResponseEntity<TokensDto> {
        val res = tokenService.refreshTokens(refreshToken)
        return ResponseEntity.ok(res)
    }

}