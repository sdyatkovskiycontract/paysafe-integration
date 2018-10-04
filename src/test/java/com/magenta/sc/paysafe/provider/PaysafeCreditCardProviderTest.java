package com.magenta.sc.paysafe.provider;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.credit_cards.CreditCardProvider;
import com.magenta.sc.exception.CreditCardException;
import com.magenta.sc.paysafe.mock.data.MockCardData;
import com.magenta.sc.paysafe.mock.data.MockEntityManagerData;
import com.magenta.sc.paysafe.mock.mockito.CreditCardFactory;
import javafx.util.Pair;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.util.Collection;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PaysafeCreditCardProvider implementation.
 */
public class PaysafeCreditCardProviderTest {

    private CreditCard cardWithFunds;
    private CreditCard cardWithoutFunds;
    private CreditCard expiredCard;
    private CreditCard invalidCard;

    private CreditCardProvider provider;

    private CreditCardProvider createProvider()
            throws PaysafeCreditCardProviderException {
        return PaysafeCreditCardProviderFactory.getInstance().create("config.properties");
    }


    @BeforeMethod
    public void setUp() throws PaysafeCreditCardProviderException {

        this.cardWithFunds = CreditCardFactory.fromCSV(
                "Bob Money;   4444333322221111; 123; 0; Not Expired");
        this.cardWithoutFunds = CreditCardFactory.fromCSV(
                "Bob Poor;    4444333322221111; 123; 0; Not Expired");
        this.expiredCard = CreditCardFactory.fromCSV(
                "Bob Expired; 4444333322221111; 123; 0; Expired");
        this.invalidCard = CreditCardFactory.fromCSV(
                "Bob Badchecksum;    4444333322221112; 123; 0; Not Expired");

        this.provider = createProvider();
    }

    @AfterMethod
    public void tearDown() {
    }

    private void testCardValid(CreditCardProvider provider, CreditCard card, boolean expected, String message) {
        try {
            boolean validValue = this.provider.isValidCard(card, card.getCompanyId(), null);
            Assert.assertEquals(validValue, expected, message);
        } catch (CreditCardException e) {
            Assert.fail(message, e);
        }
    }

    @Test
    public void testIsValidCard() {
        testCardValid(this.provider, this.cardWithFunds, true, "Card with funds test");
        testCardValid(this.provider, this.cardWithoutFunds, true, "Card without funds test");
        testCardValid(this.provider, this.expiredCard, false, "Expired card test");
        testCardValid(this.provider, this.invalidCard, false, "Invalid card test");
    }

    @Test
    public void testRegisterCreditCard() {

        // TODO: Check case when cvv and zip code check is required.

        CreditCard testingCard = this.cardWithFunds;

        Long companyId = 123l;

        final MockCardData mockCardData = new MockCardData();
        final MockEntityManagerData mockEmInfo = new MockEntityManagerData();

        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            Long id = (Long)args[0];
            mockCardData.setCompanyId(id);
            return null;
        }).when(testingCard).setCompanyId(anyLong());

        EntityManager em = mock(EntityManager.class);
        when(em.merge(any(CreditCard.class))).thenAnswer(
                (Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    CreditCard card = (CreditCard)args[0];
                    mockEmInfo.setCardMerged(card);
                    return (CreditCard)args[0];
                });

        try {
            Pair<CreditCard, Collection<CreditCardTransaction>> res =
                this.provider.registerCreditCard(
                        testingCard,
                        companyId,
                        em,
                        false,
                        false);

            Assert.assertEquals(mockEmInfo.getCardMerged(), testingCard);
            Assert.assertEquals(companyId, mockCardData.getCompanyId());

            // TODO: Check that profileId has been added
        }
        catch (CreditCardException e) {
            Assert.fail();
        }
    }

    @Test
    public void testSettleTransaction() {
        try {
            CreditCardTransaction transaction =
                    this.provider.settleTransaction(
                            this.cardWithFunds,
                            "some-GUID",
                            1.00,
                            "invoice",
                            null);

            Assert.assertNotNull(transaction);
        } catch (CreditCardException e) {
            Assert.fail("Failed", e);
        }
    }

    @Test
    public void testLockAdditionalAmount() {
    }

    @Test
    public void testLockPayment() {
    }

    @Test
    public void testDeleteToken() {
    }

    @Test
    public void testAuthoriseAndSubmitByToken() {
    }

    @Test
    public void testAuthoriseAndRefundByToken() {
    }

    @Test
    public void testGetTransactions() {
    }

    @Test
    public void testGetMaxAuthorisedDays() {
    }

    @Test
    public void testIsCorrectTransaction() {
    }
}