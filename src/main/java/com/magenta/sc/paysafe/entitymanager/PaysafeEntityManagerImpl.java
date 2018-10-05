package com.magenta.sc.paysafe.entitymanager;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;

public class PaysafeEntityManagerImpl implements PaysafeEntityManager {
    private final EntityManager entityManager;

    PaysafeEntityManagerImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addRegistrationInfo(CreditCard card, Long companyId, String token) {
        card.setCompanyId(companyId);
        card.setToken(token);
        this.entityManager.merge(card);
    }

    @Override
    public void removeCardRegistrationInfo(CreditCard card) {
        card.setToken(null);
        this.entityManager.merge(card);
    }

    @Override
    public CreditCardTransaction createCardAuthTransaction(CreditCard card, String merchantRefNum, String authId, String authCode, DateTime txnTime) {
        return null;
    }
}
