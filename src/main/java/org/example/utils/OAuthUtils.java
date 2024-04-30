package org.example.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class OAuthUtils {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OAuthUtils.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generate a random client UUID
     *
     * @return String
     */
    public static UUID generateClientUid() {
        UUID randomClientUid = UUID.randomUUID();
        logger.info("Generating client UID: " + randomClientUid);
        return randomClientUid;
    }

    /**
     * Generate a random client secret
     *
     * @return String
     */
    public static String generateClientSecret() {
        byte[] bytes = new byte[32]; // 256-bit
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * This method generates a unique access token for OAuth 2.0 authentication.
     * It creates a new BigInteger instance with a bit length of 130 using a SecureRandom object as the source of randomness.
     * The bit length of 130 ensures the uniqueness of the generated access token.
     * The BigInteger value is then converted to a string representation in base 32, resulting in a URL-safe string.
     *
     * @return A unique, URL-safe access token.
     */
    public static String generateAccessToken() {
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(130, secureRandom).toString(32);
    }

    /**
     * Generate expiration date for the token given seconds.
     * @param seconds
     * @return
     */
    public static Date getExpirationDate(int seconds) {
        return new Date(System.currentTimeMillis() + seconds * 1000);
    }


}
