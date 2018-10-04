package com.magenta.sc.paysafe.provider.verification;

import com.magenta.sc.core.entity.customer.CreditCard;

public class CardVerificationParameters {
    private final CreditCard card;
    private final String merchantRefNum;
    private final boolean checkCvv;
    private final boolean checkPostcode;

    public CardVerificationParameters(CreditCard card, String merchantRefNum, boolean checkCvv, boolean checkPostcode) {
        this.card = card;
        this.merchantRefNum = merchantRefNum;
        this.checkCvv = checkCvv;
        this.checkPostcode = checkPostcode;
    }

    public CreditCard getCard() {
        return card;
    }

    public String getMerchantRefNum() {
        return merchantRefNum;
    }

    public boolean isCheckCvv() {
        return checkCvv;
    }

    public boolean isCheckPostcode() {
        return checkPostcode;
    }
}
