package com.magenta.sc.paysafe.mock.impl;

import com.magenta.sc.paysafe.entitymanager.PaysafeEntityManager;
import com.magenta.sc.paysafe.entitymanager.PaysafeEntityManagerFactory;

import javax.persistence.EntityManager;

public class PaysafeEntityManagerMockFactory implements PaysafeEntityManagerFactory {

    private static PaysafeEntityManagerMockFactory instance;

    public static synchronized PaysafeEntityManagerFactory getInstance() {
        if (instance == null)
            instance = new PaysafeEntityManagerMockFactory();
        return instance;
    }


    @Override
    public PaysafeEntityManager getPaysafeEntityManager(EntityManager em) {
        return new PaysafeEntityManagerMockImpl();
    }
}
