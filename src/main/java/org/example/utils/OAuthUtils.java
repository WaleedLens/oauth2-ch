package org.example.utils;

import java.security.SecureRandom;
import java.util.Base64;
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






}
