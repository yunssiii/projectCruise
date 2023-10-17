package com.cruise.project_cruise.token;


import org.springframework.context.annotation.Bean;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    // Define the desired length of the password
    private static final int PASSWORD_LENGTH = 10;

    public static String generateRandomPassword() {
        byte[] randomBytes = new byte[PASSWORD_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }
}