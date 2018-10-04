package com.magenta.sc.paysafe.entitymanager;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.paysafe.mock.mockito.EntityManagerFactory;
import com.magenta.sc.paysafe.mock.data.MockCardData;
import com.magenta.sc.paysafe.mock.data.MockEntityManagerData;
import com.magenta.sc.paysafe.mock.mockito.CreditCardFactory;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaysafeEntityManagerImplTest {

    private static final Long COMPANY_ID = 123l;
    private static final String PROFILE_ID = "profile-123";

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testAddRegistrationInfo() {

        final MockCardData mockCardData = new MockCardData();
        CreditCard card = CreditCardFactory.fromCSV(
            "Bob Usovich; 4444333322221111; 123; 0; Not Expired; 000000",
            mockCardData);

        final MockEntityManagerData mockEmInfo = new MockEntityManagerData();
        EntityManager entityManager = EntityManagerFactory.create(mockEmInfo);

        PaysafeEntityManager em = PaysafeEntityManagerHibernateFactory
                .getInstance().getPaysafeEntityManager(entityManager);

        em.addRegistrationInfo(card, COMPANY_ID, PROFILE_ID);

        Assert.assertEquals(mockEmInfo.getCardMerged(), card);
        Assert.assertEquals(COMPANY_ID, mockCardData.getCompanyId());
    }

    @Test
    public void testRemoveCardRegistrationInfo() {

        final MockCardData mockCardData = new MockCardData();
        CreditCard card = CreditCardFactory.fromCSV(
                "Bob Usovich; 4444333322221111; 123; 0; Not Expired; 000000",
                mockCardData);

        final MockEntityManagerData mockEntityManagerData = new MockEntityManagerData();
        EntityManager entityManager = EntityManagerFactory.create(mockEntityManagerData);


        PaysafeEntityManager em = PaysafeEntityManagerHibernateFactory
                .getInstance().getPaysafeEntityManager(entityManager);

        em.removeCardRegistrationInfo(card);

        Assert.assertEquals(mockEntityManagerData.getCardMerged(), card);
        Assert.assertNull(mockCardData.getCompanyId());
    }
}