package com.etaskify.ms.srp.service

import com.etaskify.ms.srp.crypto.*
import com.nimbusds.srp6.BigIntegerUtils
import com.nimbusds.srp6.SRP6ClientCredentials
import org.springframework.stereotype.Service

@Service
class GeneratorService(
        var srpHelper: SRP6Helper
) {

    fun generateSaltAndVerifier(req: SaltVerifierGeneratorDto): Pair<String, String>{
        val result = getSaltAndVerifierForUser(req.login, req.password)
        return result
    }

    fun srpStep1(step1: SrpStep1Dto):SrpStep1RespDto {
        val srpSession = generateSRP6ServerSession()

        val v = BigIntegerUtils.fromHex(step1.verifier)
        val salt = BigIntegerUtils.fromHex(step1.salt)

        val srp1B = srpSession.step1(step1.login, salt, v)

        return SrpStep1RespDto(
                srpB = BigIntegerUtils.toHex(srp1B),
                srpS = BigIntegerUtils.toHex(salt)
        )
    }
    fun srpStep2(req: SrpSecondStepReqDto){
        val a = BigIntegerUtils.fromHex(req.srpA)
        val m1 = BigIntegerUtils.fromHex(req.srpM1)
    }

    fun generateForSrpStep2(req: SrpStep2Dto): Step2Result{
        val resut = forStep2(req.login, req.password, req.srpS, req.srpB)
        return resut
    }

}

data class SaltVerifierGeneratorDto(
        var login: String,
        var password: String
)

data class SrpStep1Dto(
        var salt: String,
        var verifier: String,
        var login: String
)

data class SrpStep1RespDto(
        var srpB: String,
        var srpS: String
)

data class SrpStep2Dto(
        var srpS: String,
        var srpB: String,
        var login: String,
        var username: String,
        var password: String
)

data class SrpSecondStepReqDto(
        val login: String,
        val srpA: String,
        val srpM1: String
)

