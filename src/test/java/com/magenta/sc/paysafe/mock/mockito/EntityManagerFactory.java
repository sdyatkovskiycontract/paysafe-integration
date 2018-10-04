package com.magenta.sc.paysafe.mock.mockito;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;

public class EntityManagerFactory {
    public static EntityManager create() {
        EntityManager em = mock(EntityManager.class);
        return em;
    }
}
