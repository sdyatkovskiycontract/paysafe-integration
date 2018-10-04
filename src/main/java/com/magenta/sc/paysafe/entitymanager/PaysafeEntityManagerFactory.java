package com.magenta.sc.paysafe.entitymanager;

import javax.persistence.EntityManager;
import java.util.HashMap;

public class PaysafeEntityManagerFactory {
    private static PaysafeEntityManagerFactory instance;

    // Just cache implementation, instead of creating it each
    // time we need to do something with entities.
    private HashMap<EntityManager, PaysafeEntityManager> managers = new HashMap<>();

    public static synchronized PaysafeEntityManagerFactory getInstance() {
        if (instance == null)
            instance = new PaysafeEntityManagerFactory();
        return instance;
    }

    public synchronized PaysafeEntityManager getPaysafeEntityManager(EntityManager em) {

        if (managers.containsKey(em))
            return managers.get(em);

        PaysafeEntityManager paysafeEntityManager = new PaysafeEntityManagerImpl(em);
        managers.put(em, paysafeEntityManager);

        return paysafeEntityManager;
    }
}
