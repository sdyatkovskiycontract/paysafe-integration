package com.magenta.sc.paysafe.mock.impl;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.paysafe.entitymanager.PaysafeEntityManager;

import java.util.HashMap;
import java.util.Map;

public class PaysafeEntityManagerMockImpl implements PaysafeEntityManager {

    Map<String, CreditCard> cards = new HashMap<>();

    @Override
    public void addRegistrationInfo(CreditCard card, Long companyId, String token) {
        if (!this.cards.containsKey(card.getNumber()))
            cards.put(card.getNumber(), card);
        else
            card = cards.get(card.getNumber());

        card.setCompanyId(companyId);
        card.setToken(token);
    }

    @Override
    public void removeCardRegistrationInfo(CreditCard card) {
        if (this.cards.containsKey(card.getNumber()))
            card = cards.get(card.getNumber());

        card.setToken(null);
    }
}
