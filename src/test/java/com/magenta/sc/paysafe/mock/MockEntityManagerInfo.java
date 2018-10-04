package com.magenta.sc.paysafe.mock;

import com.magenta.sc.core.entity.customer.CreditCard;

public class MockEntityManagerInfo {
    private CreditCard cardMerged;

    public CreditCard getCardMerged() {
        return cardMerged;
    }

    public void setCardMerged(CreditCard cardMerged) {
        this.cardMerged = cardMerged;
    }
}
