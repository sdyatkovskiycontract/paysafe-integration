package com.magenta.sc.paysafe.helpers;

public class CardTokenUtils {

    private static final String SEPARATOR = ";";
    private static int PROFILE_ID_IDX = 0;
    private static int PAYMENT_TOKEN_IDX = 1;

    public static String buildCardToken(String profileId, String paymentToken) {
        return String.format("%s%s%s", profileId, SEPARATOR, paymentToken);
    }

    public static String getProfileId(String token) {
        String[] parts = token.split(SEPARATOR);
        if (parts.length != 2)
            throw new IllegalArgumentException("Invalid credit card token");
        return parts[PROFILE_ID_IDX];
    }

    public static String getPaymentToken(String token) {
        String[] parts = token.split(SEPARATOR);
        if (parts.length != 2)
            throw new IllegalArgumentException("Invalid credit card token");
        return parts[PAYMENT_TOKEN_IDX];
    }

}
