package com.magenta.sc.paysafe.entitymanager;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import org.joda.time.DateTime;

public interface PaysafeEntityManager {
    void addRegistrationInfo(CreditCard card, Long companyId, String token);
    void removeCardRegistrationInfo(CreditCard card);
    CreditCardTransaction createCardAuthTransaction(
            CreditCard card,
            String merchantRefNum,
            String authId,
            String authCode,
            DateTime txnTime);
}
