package com.magenta.sc.paysafe.helpers;

import java.util.UUID;

public class MerchantRefNumber {
    public static String generate() {
        String merchantRefNum = "MerchantRef-" + UUID.randomUUID().toString().substring(0,20);
        return merchantRefNum;
    }
}
