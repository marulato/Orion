package org.orion.common.validation;

import java.util.Date;

public class Token {

    private static String token;
    private static Date generateDate;
    private static Date invalidDate;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Token.token = token;
    }

    public static Date getGenerateDate() {
        return generateDate;
    }

    public static void setGenerateDate(Date generateDate) {
        Token.generateDate = generateDate;
    }

    public static Date getInvalidDate() {
        return invalidDate;
    }

    public static void setInvalidDate(Date invalidDate) {
        Token.invalidDate = invalidDate;
    }
}
