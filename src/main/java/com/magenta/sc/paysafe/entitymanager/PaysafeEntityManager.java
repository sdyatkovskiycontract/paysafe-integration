package com.magenta.sc.paysafe.entitymanager;

import com.magenta.sc.core.entity.customer.CreditCard;

public interface PaysafeEntityManager {
    void addRegistrationInfo(CreditCard card, Long companyId, String token);
    void removeCardRegistrationInfo(CreditCard card);
}
