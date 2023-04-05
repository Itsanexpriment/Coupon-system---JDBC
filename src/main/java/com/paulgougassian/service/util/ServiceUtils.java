package com.paulgougassian.service.util;

public class ServiceUtils {
    private static final String PASSWORD_REGEX = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}";

    public static boolean isPasswordValid(String password) {
        return password.matches(PASSWORD_REGEX);
    }
}
