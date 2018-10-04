package com.magenta.sc.paysafe.mock.mockito;

import com.magenta.sc.core.entity.customer.CreditCard;
import org.mockito.stubbing.Answer;

import javax.persistence.EntityManager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityManagerFactory {
    public static EntityManager create() {
        EntityManager em = mock(EntityManager.class);
        return em;
    }
}
