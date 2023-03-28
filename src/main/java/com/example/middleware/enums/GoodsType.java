package com.example.middleware.enums;

public enum GoodsType {

    PARCEL("1"),
    DOCUMENT("2");

    private final String value;

    GoodsType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static boolean isValid(String value) {
        try {
            GoodsType.valueOf(value);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
