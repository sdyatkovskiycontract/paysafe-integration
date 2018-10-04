package com.magenta.sc.paysafe.entitymanager;

import javax.persistence.EntityManager;

public interface PaysafeEntityManagerFactory {
    PaysafeEntityManager getPaysafeEntityManager(EntityManager em);
}
