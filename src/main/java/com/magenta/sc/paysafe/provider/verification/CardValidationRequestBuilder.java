package com.magenta.sc.paysafe.provider.verification;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.exception.CreditCardException;
import com.magenta.sc.paysafe.helpers.HolderNameInfo;
import com.paysafe.cardpayments.Card;
import com.paysafe.cardpayments.Verification;

public class CardValidationRequestBuilder {

    public static Verification build(CardVerificationParameters parameters) throws CreditCardException {

        CreditCard card = parameters.getCard();
        String merchantRefNum = parameters.getMerchantRefNum();

        String holderName = card.getHolderName();

        HolderNameInfo holder = HolderNameInfo.fromString(holderName);

        Verification.VerificationBuilder builder = Verification.builder();
        Card.CardBuilder<Verification.VerificationBuilder> cardBuilder = builder.card();

        cardBuilder.cardNum(card.getNumber())
            .cardExpiry()
                .month(card.getExpireDate().getMonthOfYear())
                .year(card.getExpireDate().getYearOfEra())
            .done();

        if (parameters.isCheckCvv())
            cardBuilder.cvv(card.getSecureCode());

        builder
            .merchantRefNum(merchantRefNum)
            .profile()
                .firstName(holder.getFirstName())
                .lastName(holder.getLastName())
            .done()
            .billingDetails()
                .zip(card.getPostcode())
            .done();

        return builder.build();
    }
}
