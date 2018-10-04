package com.magenta.sc.paysafe.mock.mockito;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.paysafe.mock.data.MockCardData;
import com.magenta.sc.paysafe.mock.data.MockEntityManagerData;
import org.mockito.stubbing.Answer;

import javax.persistence.EntityManager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityManagerFactory {

    private static void setupData(EntityManager em, MockEntityManagerData data) {
        when(em.merge(any(CreditCard.class))).thenAnswer(
                (Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    CreditCard card = (CreditCard)args[0];
                    data.setCardMerged(card);
                    return (CreditCard)args[0];
                });
    }

    public static EntityManager create() {
        EntityManager em = create(null);
        return em;
    }

    public static EntityManager create(MockEntityManagerData data) {
        EntityManager em = mock(EntityManager.class);
        if (data != null)
            setupData(em, data);
        return em;
    }
}
