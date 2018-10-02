package com.magenta.sc;

public final class StringHelper {
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String uncapitalizeFirstLetter(String value) {
        if (value == null) {
            return null;
        } else {
            return value.length() == 0 ? value : (new StringBuilder(value.length())).append(Character.toLowerCase(value.charAt(0))).append(value.substring(1)).toString();
        }
    }

    public static String capitalizeFirstLetter(String value) {
        if (value == null) {
            return null;
        } else {
            return value.length() == 0 ? value : (new StringBuilder(value.length())).append(Character.toUpperCase(value.charAt(0))).append(value.substring(1)).toString();
        }
    }
}
