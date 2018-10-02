package com.magenta.sc.paysafe.provider;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.credit_cards.CreditCardProvider;
import com.magenta.sc.exception.CreditCardException;
import com.magenta.sc.paysafe.mock.core.entity.customer.CreditCardFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for PaysafeCreditCardProvider implementation.
 */
public class PaysafeCreditCardProviderTest {

    private CreditCard cardWithFunds;
    private CreditCard cardWithoutFunds;
    private CreditCard expiredCard;
    private CreditCard invalidCard;

    private PaysafeCreditCardProvider createProvider() {
        return new PaysafeCreditCardProvider();
    }

    @BeforeMethod
    public void setUp() {

        cardWithFunds = CreditCardFactory.fromCSV(
                "Bob Money; 4444333322221111; 123; 0; Not Expired");
        cardWithoutFunds = CreditCardFactory.fromCSV(
                "Bob Poor; 4444333322221111; 123; 0; Not Expired");
        expiredCard = CreditCardFactory.fromCSV(
                "Bob Expired; 4444333322221111; 123; 0; Not Expired");
        invalidCard = CreditCardFactory.fromCSV(
                "Bob Fake; 4444333322221111; 123; 0; Not Expired");
    }

    @AfterMethod
    public void tearDown() {
    }


    private void testCardValid(CreditCardProvider provider, CreditCard card, boolean expected, String message) {
        try {
            Boolean validValue = provider.isValidCard(cardWithFunds, cardWithFunds.getCompanyId(), null);
            Assert.assertTrue(validValue, message);
        } catch (CreditCardException e) {
            Assert.fail(message, e);
        }
    }

    @Test
    public void testIsValidCard() {

        CreditCardProvider provider = createProvider();

        testCardValid(provider, cardWithFunds, true, "Card with funds test");
        testCardValid(provider, cardWithoutFunds, true, "Card without funds test");
        testCardValid(provider, expiredCard, false, "Expired card test");
        testCardValid(provider, invalidCard, false, "Invalid card test");
    }

    @Test
    public void testRegisterCreditCard() {
    }

    @Test
    public void testSettleTransaction() {
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