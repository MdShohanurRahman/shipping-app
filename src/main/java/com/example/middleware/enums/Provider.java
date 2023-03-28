package com.example.middleware.enums;

import com.example.middleware.exception.ApiException;

public enum Provider {
    CITY_LINK, JT_EXPRESS;

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
