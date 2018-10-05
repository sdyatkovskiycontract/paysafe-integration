package com.magenta.sc.paysafe.provider.authorization;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.paysafe.cardpayments.Authorization;

public class AuthorizationRequestBuilder {
    public static Authorization build(CardAuthorizationParameters p) {

        CreditCard card = p.getCard();
        // Build our order object.
        Authorization authRequest = Authorization.builder()
                .merchantRefNum(p.getMerchantRefNum())
                .amount(p.getAmount())
                .settleWithAuth(p.isSettleWithAuth()) // False, means, that we don't want to settle transaction.
                .card()
                .cardNum(card.getNumber())
                .cvv(card.getSecureCode())
                .cardExpiry()
                .month(card.getExpireDate().getMonthOfYear())
                .year(card.getExpireDate().getYearOfEra())
                .done()
                .done()
                .billingDetails()
                .state("Moscow")// TODO: get rid of those fields
                .country("RU")
                .zip("000000")
                .done()
                .build();

        return authRequest;
    }
}
