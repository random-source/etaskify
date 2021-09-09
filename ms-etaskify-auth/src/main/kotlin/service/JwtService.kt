package com.etaskify.ms.auth.service

import com.etaskify.ms.auth.model.AccessTokenClaimsSet
import com.etaskify.ms.auth.model.RefreshTokenClaimsSet
import com.etaskify.ms.auth.exception.AuthException
import com.google.gson.Gson
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec

@Service
class JwtService(
        private val gson: Gson
) {

    companion object {
        private const val ALGORITHM: String = "RSA"
        private const val KEY_SIZE = 2048
    }

    fun generateKeyPair(): KeyPair {
        val gen = KeyPairGenerator.getInstance(ALGORITHM)
        gen.initialize(KEY_SIZE)
        return gen.generateKeyPair()
    }

    fun generateToken(claimsSet: AccessTokenClaimsSet, privateKey: PrivateKey): String {
        return generateSignedJWT(gson.toJson(claimsSet), privateKey).serialize()
    }

    fun generateToken(claimsSet: RefreshTokenClaimsSet, privateKey: PrivateKey): String {
        return generateSignedJWT(gson.toJson(claimsSet), privateKey).serialize()
    }

    private fun generateSignedJWT(tokenClaimSetJson: String, privateKey: PrivateKey): SignedJWT {
        val jwtClaimsSet: JWTClaimsSet = JWTClaimsSet.parse(tokenClaimSetJson)
        val header = JWSHeader(JWSAlgorithm.RS256)
        val signedJWT = SignedJWT(header, jwtClaimsSet)
        val signer = RSASSASigner(privateKey)
        signedJWT.sign(signer)
        return signedJWT
    }

    fun getClaimsFromRefreshToken(token: String): RefreshTokenClaimsSet {
        return gson.fromJson(getClaimsFromToken(token).toString(), RefreshTokenClaimsSet::class.java)
    }

    fun getClaimsFromAccessToken(token: String): AccessTokenClaimsSet {
        return gson.fromJson(getClaimsFromToken(token).toString(), AccessTokenClaimsSet::class.java)
    }

    private fun getClaimsFromToken(token: String): JWTClaimsSet {
        return SignedJWT.parse(token).jwtClaimsSet
    }

    fun verifyToken(token: String, publicKey: String) {
        val key = KeyFactory.getInstance("RSA").generatePublic(
                X509EncodedKeySpec(Base64Utils.decodeFromString(publicKey))
        ) as RSAPublicKey
        val signedJwt = SignedJWT.parse(token)
        val verifier: JWSVerifier = RSASSAVerifier(key)
        if (!signedJwt.verify(verifier)) {
            throw AuthException("error.jwtSignature.invalid", "JWT signature verification failed")
        }
    }

}