package com.magenta.sc.paysafe.entitymanager;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.paysafe.mock.EntityManagerFactory;
import com.magenta.sc.paysafe.mock.MockCardInfo;
import com.magenta.sc.paysafe.mock.MockEntityManagerInfo;
import com.magenta.sc.paysafe.mock.core.entity.customer.CreditCardFactory;
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

    private CreditCard card;
    private EntityManager entityManager;

    private static final Long COMPANY_ID = 123l;
    private static final String PROFILE_ID = "profile-123";


    private void setupCard(CreditCard card, MockCardInfo cardInfo) {
        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            Long id = (Long)args[0];
            cardInfo.setCompanyId(id);
            return null;
        }).when(card).setCompanyId(anyLong());
    }

    private void setupEm(EntityManager em, MockEntityManagerInfo mockEmInfo) {
        when(em.merge(any(CreditCard.class))).thenAnswer(
                (Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    CreditCard card = (CreditCard)args[0];
                    mockEmInfo.setCardMerged(card);
                    return (CreditCard)args[0];
                });
    }

    @BeforeMethod
    public void setUp() {
        this.card = CreditCardFactory.fromCSV(
                "Bob Usovich; 4444333322221111; 123; 0; Not Expired");
        this.entityManager = EntityManagerFactory.create();
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testAddRegistrationInfo() {
        final MockCardInfo mockCardInfo = new MockCardInfo();
        final MockEntityManagerInfo mockEmInfo = new MockEntityManagerInfo();

        setupCard(this.card, mockCardInfo);
        setupEm(this.entityManager, mockEmInfo);

        PaysafeEntityManager em = PaysafeEntityManagerFactory
                .getInstance().getPaysafeEntityManager(this.entityManager);

        em.addRegistrationInfo(this.card, COMPANY_ID, PROFILE_ID);

        Assert.assertEquals(mockEmInfo.getCardMerged(), this.card);
        Assert.assertEquals(COMPANY_ID, mockCardInfo.getCompanyId());
    }

    @Test
    public void testRemoveCardRegistrationInfo() {
        final MockCardInfo mockCardInfo = new MockCardInfo();
        final MockEntityManagerInfo mockEmInfo = new MockEntityManagerInfo();

        setupCard(this.card, mockCardInfo);
        setupEm(this.entityManager, mockEmInfo);

        PaysafeEntityManager em = PaysafeEntityManagerFactory
                .getInstance().getPaysafeEntityManager(this.entityManager);

        em.removeCardRegistrationInfo(this.card);

        Assert.assertEquals(mockEmInfo.getCardMerged(), this.card);
        Assert.assertNull(mockCardInfo.getCompanyId());
    }
}