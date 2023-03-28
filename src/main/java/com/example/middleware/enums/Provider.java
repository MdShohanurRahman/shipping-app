package com.example.middleware.enums;

public enum Provider {
    CITY_LINK_EXPRESS, JT_EXPRESS;

    public static boolean isValid(String value) {
        try {
            Provider.valueOf(value);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
