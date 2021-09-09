package com.etaskify.ms.srp.crypto;

import com.nimbusds.srp6.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * SRP6a routines for compatibility with Thinbus js SRP implementation
 */
@SuppressWarnings("WeakerAccess")
public class SRP6ThinbusRoutines {

    private SRP6ThinbusRoutines() {
    }

    public static XRoutine getXRoutine() {
        return (XRoutine & Serializable) (digest, salt, username, password) -> {
            digest.reset();
            digest.update(username);
            digest.update((byte) 58);
            digest.update(password);
            byte[] h1 = digest.digest();
            while (h1[0] == 0) {
                h1 = Arrays.copyOfRange(h1, 1, h1.length);
            }
            String h1S = BigIntegerUtils.toHex(new BigInteger(1, h1));

            digest.reset();
            digest.update((BigIntegerUtils.toHex(new BigInteger(1, salt)) + h1S).toUpperCase().getBytes());
            byte[] xBytes = digest.digest();

            while (xBytes[0] == 0) {
                xBytes = Arrays.copyOfRange(xBytes, 1, xBytes.length);
            }

            BigInteger x = BigIntegerUtils.bigIntegerFromBytes(xBytes);
            x = x.mod(SRP6CryptoParams.N_256);
            return x;
        };
    }

    public static ClientEvidenceRoutine getClientEvidenceRoutine() {
        return (ClientEvidenceRoutine & Serializable) (srp6CryptoParams, srp6ClientEvidenceContext) -> {
            MessageDigest digest = srp6CryptoParams.getMessageDigestInstance();
            digest.update(BigIntegerUtils.toHex(srp6ClientEvidenceContext.A).getBytes());
            digest.update(BigIntegerUtils.toHex(srp6ClientEvidenceContext.B).getBytes());
            digest.update(BigIntegerUtils.toHex(srp6ClientEvidenceContext.S).getBytes());
            return BigIntegerUtils.bigIntegerFromBytes(digest.digest());
        };
    }

    public static URoutine getURoutine() {
        return (URoutine & Serializable) (srp6CryptoParams, uRoutineContext) -> {
            MessageDigest digest = srp6CryptoParams.getMessageDigestInstance();
            digest.update(BigIntegerUtils.toHex(uRoutineContext.A).getBytes());
            digest.update(BigIntegerUtils.toHex(uRoutineContext.B).getBytes());
            return BigIntegerUtils.bigIntegerFromBytes(digest.digest());
        };
    }

    public static ServerEvidenceRoutine getServerEvidenceRoutine() {
        return (ServerEvidenceRoutine & Serializable) (srp6CryptoParams, srp6ServerEvidenceContext) -> {
            MessageDigest digest = srp6CryptoParams.getMessageDigestInstance();
            digest.update(BigIntegerUtils.toHex(srp6ServerEvidenceContext.A).getBytes());
            digest.update(BigIntegerUtils.toHex(srp6ServerEvidenceContext.M1).getBytes());
            digest.update(BigIntegerUtils.toHex(srp6ServerEvidenceContext.S).getBytes());
            return BigIntegerUtils.bigIntegerFromBytes(digest.digest());
        };
    }

}
