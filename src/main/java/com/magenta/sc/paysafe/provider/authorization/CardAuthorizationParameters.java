package com.magenta.sc.paysafe.provider.authorization;

import com.magenta.sc.core.entity.customer.CreditCard;

public class CardAuthorizationParameters {

    private final CreditCard card;
    private final String merchantRefNum;
    private final Integer amount;
    private final boolean settleWithAuth;

    public CardAuthorizationParameters(CreditCard card,
                                       String merchantRefNum,
                                       Integer amountInt,
                                       boolean settleWithAuth) {
        this.card = card;
        this.merchantRefNum = merchantRefNum;
        this.amount = amountInt;
        this.settleWithAuth = settleWithAuth;
    }

    public CreditCard getCard() {
        return card;
    }

    public String getMerchantRefNum() {
        return merchantRefNum;
    }

    public Integer getAmount() {
        return amount;
    }

    public boolean isSettleWithAuth() {
        return settleWithAuth;
    }
}
