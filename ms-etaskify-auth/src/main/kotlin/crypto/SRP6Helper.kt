package com.etaskify.ms.auth.crypto

import com.etaskify.ms.auth.crypto.SRP6Helper.Companion.CRYPTO_PARAMS
import com.nimbusds.srp6.BigIntegerUtils
import com.nimbusds.srp6.SRP6ClientSession
import com.nimbusds.srp6.SRP6CryptoParams
import com.nimbusds.srp6.SRP6ServerSession
import com.nimbusds.srp6.SRP6VerifierGenerator
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class SRP6Helper {

    companion object {
        val CRYPTO_PARAMS = SRP6CryptoParams(SRP6CryptoParams.N_256, SRP6CryptoParams.g_common, "SHA-256")
    }
}

    fun generateSRP6ClientSession(): SRP6ClientSession {
        val srp6ClientSession = SRP6ClientSession()
        srp6ClientSession.xRoutine = SRP6ThinbusRoutines.getXRoutine()
        srp6ClientSession.hashedKeysRoutine = SRP6ThinbusRoutines.getURoutine()
        srp6ClientSession.clientEvidenceRoutine = SRP6ThinbusRoutines.getClientEvidenceRoutine()
        srp6ClientSession.serverEvidenceRoutine = SRP6ThinbusRoutines.getServerEvidenceRoutine()
        return srp6ClientSession
    }

    fun generateSRP6ServerSession(): SRP6ServerSession {
        val srp6ServerSession = SRP6ServerSession(CRYPTO_PARAMS)
        srp6ServerSession.clientEvidenceRoutine = SRP6ThinbusRoutines.getClientEvidenceRoutine()
        srp6ServerSession.hashedKeysRoutine = SRP6ThinbusRoutines.getURoutine()
        srp6ServerSession.serverEvidenceRoutine = SRP6ThinbusRoutines.getServerEvidenceRoutine()
        return srp6ServerSession
    }

    fun generateSRP6VerifierGenerator(): SRP6VerifierGenerator {
        val srp6VerifierGenerator = SRP6VerifierGenerator(CRYPTO_PARAMS)
        srp6VerifierGenerator.xRoutine = SRP6ThinbusRoutines.getXRoutine()
        return srp6VerifierGenerator
    }

    fun generateRandomSalt(): BigInteger {
        val gen = generateSRP6VerifierGenerator()
        return BigInteger(1, gen.generateRandomSalt())
    }

    fun generateSaltAndVerifierForUser(login: String?, password: String?): Pair<String, String> {
        val gen = generateSRP6VerifierGenerator()
        val salt = BigInteger(1, gen.generateRandomSalt())
        val verifier = gen.generateVerifier(salt, login, password)
        return Pair(
                BigIntegerUtils.toHex(salt),
                BigIntegerUtils.toHex(verifier)
        )
    }
