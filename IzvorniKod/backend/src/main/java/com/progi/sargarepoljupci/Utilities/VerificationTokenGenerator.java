package com.progi.sargarepoljupci.Utilities;

import java.util.UUID;

public class VerificationTokenGenerator {

    public static String generateUniqueVerificationToken() {
        return UUID.randomUUID().toString();
    }
}