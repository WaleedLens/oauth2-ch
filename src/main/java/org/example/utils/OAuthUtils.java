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

    /**
     * Generate a random authorization code
     *
     * @return String
     */
    public static String generateAuthorizationCode() {
        // Generate a secure random authorization code
        SecureRandom secureRandom = new SecureRandom();
        byte[] code = new byte[20];
        secureRandom.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }


    /**
     * This method generates a unique refresh token for OAuth 2.0 authentication.
     * It creates a new BigInteger instance with a bit length of 160 using a SecureRandom object as the source of randomness.
     * The bit length of 160 ensures the uniqueness of the generated refresh token.
     * The BigInteger value is then converted to a string representation in base 32, resulting in a URL-safe string.
     *
     * @return A unique, URL-safe refresh token.
     */
    public static String generateRefreshToken() {
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(160, secureRandom).toString(32);
    }

    /**
     * Checks if a certain time period has expired.
     *
     * @param expire_seconds The duration of the time period in seconds.
     * @param created The starting point of the time period as a Date object.
     * @return A boolean value indicating whether the time period has expired. Returns true if the time period has expired, false otherwise.
     */
    public static boolean isExpired(int expire_seconds, Date created) {
        // Create a new Date object representing the current time
        Date now = new Date();

        // Calculate the difference in milliseconds between the current time and the created time
        long diff = now.getTime() - created.getTime();

        // Convert the difference to seconds
        long diffSeconds = diff / 1000 % 60;

        // Return true if the difference in seconds is greater than the expire_seconds parameter, false otherwise
        return diffSeconds > expire_seconds;
    }

}
