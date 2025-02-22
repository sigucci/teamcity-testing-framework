package com.example.teamcity.api.generator;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public final class RandomData {
    private static final String TEST_PREFIX = "test_";
    private static final int MAX_LENTGH = 10;

    public static String getString() {
        return TEST_PREFIX + RandomStringUtils.randomAlphanumeric(MAX_LENTGH);
    }

    public static String getString(int length) {
        return TEST_PREFIX + RandomStringUtils
                .randomAlphanumeric(Math.max(length - TEST_PREFIX.length(), MAX_LENTGH));
    }
}
