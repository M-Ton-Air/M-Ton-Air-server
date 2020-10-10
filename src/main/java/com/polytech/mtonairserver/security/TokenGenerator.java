package com.polytech.mtonairserver.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Class that handles tokens generation for our API.
 */
public class TokenGenerator
{
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    /**
     * Generates a user api token
     * @return a user api token
     * https://stackoverflow.com/a/56628391/11284920
     */
    public static String generateUserApiToken()
    {
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
