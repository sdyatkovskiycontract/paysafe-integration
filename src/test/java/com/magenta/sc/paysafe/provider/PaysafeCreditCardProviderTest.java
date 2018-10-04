package com.magenta.sc.paysafe.provider;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.credit_cards.CreditCardProvider;
import com.magenta.sc.exception.CreditCardException;
import com.magenta.sc.paysafe.mock.data.MockCardData;
import com.magenta.sc.paysafe.mock.data.MockEntityManagerData;
import com.magenta.sc.paysafe.mock.mockito.CreditCardFactory;
import com.magenta.sc.paysafe.mock.mockito.EntityManagerFactory;
import javafx.util.Pair;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * Unit tests for PaysafeCreditCardProvider implementation.
 */
public class PaysafeCreditCardProviderTest {

    private CreditCard cardWithFunds;
    private CreditCard cardWithoutFunds;
    private CreditCard expiredCard;
    private CreditCard invalidCard;
    private CreditCard cardWithFundsWithoutPostcode;

    private CreditCardProvider provider;

    private final static Long COMPANY_ID = 1212l;

    private CreditCardProvider createProvider()
            throws PaysafeCreditCardProviderException {
        return PaysafeCreditCardProviderFactory.getInstance().create("config.properties");
    }


    @BeforeMethod
    public void setUp() throws PaysafeCreditCardProviderException {

        this.cardWithFunds = CreditCardFactory.fromCSV(
                "Bob Money;   4444333322221111; 123; 0; Not Expired; 000000");
        this.cardWithoutFunds = CreditCardFactory.fromCSV(
                "Bob Poor;    4444333322221111; 123; 0; Not Expired; 000000");
        this.expiredCard = CreditCardFactory.fromCSV(
                "Bob Expired; 4444333322221111; 123; 0; Expired; 000000");
        this.invalidCard = CreditCardFactory.fromCSV(
                "Bob Badchecksum;    4444333322221112; 123; 0; Not Expired; 000000");

        this.cardWithFundsWithoutPostcode = CreditCardFactory.fromCSV(
                "Bob Money;   4444333322221111; 123; 0; Not Expired; ");

        this.provider = createProvider();
    }

    @AfterMethod
    public void tearDown() {
    }

    private void testCardValid(CreditCardProvider provider,
                               CreditCard card,
                               boolean expected,
                               int creditCardExceptionType,
                               String message) {
        try {
            MockEntityManagerData mockEntityManagerData = new MockEntityManagerData();
            EntityManager em = EntityManagerFactory.create(mockEntityManagerData);

            boolean validValue = this.provider.isValidCard(card, card.getCompanyId(), em);

            if (creditCardExceptionType != -1)
                Assert.fail(message);

            Assert.assertEquals(validValue, expected, message);

            if (validValue)
                Assert.assertEquals(card.getCompanyId(), mockEntityManagerData.getCardMerged().getCompanyId());
        } catch (CreditCardException e) {
            Assert.assertEquals(e.getType(), creditCardExceptionType);
        }
    }

    private void testCardValid(CreditCardProvider provider,
                               CreditCard card,
                               int creditCardExceptionType,
                               String message) {
        testCardValid(provider, card, false, creditCardExceptionType, message);
    }

    private void testCardValid(CreditCardProvider provider,
                               CreditCard card,
                               boolean expected,
                               String message) {
        testCardValid(provider, card, expected, -1, message);
    }

    @Test
    public void testIsValidCard() {
        testCardValid(this.provider, this.cardWithFunds, true, "Card with funds test");
        testCardValid(this.provider, this.cardWithoutFunds, true, "Card without funds test");
        testCardValid(this.provider, this.expiredCard, false, "Expired card test");
        testCardValid(this.provider, this.invalidCard, false, "Invalid card test");
        testCardValid(this.provider, this.cardWithFundsWithoutPostcode, CreditCardException.EMPTY_POSTCODE, "Card without postcode test");
    }

    private void testRegisterCreditCard(String csvCard) {
        testRegisterCreditCard(csvCard, -1, false, false);
    }


    private void testRegisterCreditCard(String csvCard,
                                        boolean checkCvv,
                                        boolean checkPostcode) {
        testRegisterCreditCard(csvCard, -1, checkCvv, checkPostcode);
    }

    private void testRegisterCreditCard(String csvCard,
                                   int expectedCreditCardExceptionType,
                                   boolean checkCvv,
                                   boolean checkPostcode) {

        final MockCardData mockCardData = new MockCardData();
        final MockEntityManagerData mockEmInfo = new MockEntityManagerData();

        CreditCard testingCard = CreditCardFactory.fromCSV(csvCard, mockCardData);

        EntityManager em = EntityManagerFactory.create(mockEmInfo);

        try {
            Pair<CreditCard, Collection<CreditCardTransaction>> res =
                    this.provider.registerCreditCard(
                            testingCard,
                            COMPANY_ID,
                            em,
                            checkCvv,
                            checkPostcode);

            Assert.assertEquals(mockEmInfo.getCardMerged(), testingCard);
            Assert.assertEquals(COMPANY_ID, mockCardData.getCompanyId());
            Assert.assertNotNull(mockCardData.getToken());

        }
        catch (CreditCardException e) {
            if (expectedCreditCardExceptionType == -1)
                Assert.fail();
            Assert.assertEquals(e.getType(), expectedCreditCardExceptionType);
        }
    }

    @Test
    public void testRegisterCreditCard() {
        testRegisterCreditCard("Bob Money;   4444333322221111; 123; 0; Not Expired; 000000");
        testRegisterCreditCard("Bob Money;   4444333322221111; 123; 0; Not Expired; 000000", true, false);
        testRegisterCreditCard("Bob Money;   4444333322221111; 123; 0; Not Expired; 000000", true, true);
        testRegisterCreditCard("Bob Money;   4444333322221111; 7; 0; Not Expired; 000000", CreditCardException.INVALID_CARD_INFO, true, true);
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
        final MockCardData mockCardData = new MockCardData();
        final MockEntityManagerData mockEmInfo = new MockEntityManagerData();

        CreditCard testingCard = CreditCardFactory.fromCSV("Bob Money;   4444333322221111; 123; 0; Not Expired; 000000", mockCardData);

        EntityManager em = EntityManagerFactory.create(mockEmInfo);

        try {
            Pair<CreditCard, Collection<CreditCardTransaction>> res =
                    this.provider.registerCreditCard(
                            testingCard,
                            COMPANY_ID,
                            em,
                            false,
                            false);

            Assert.assertEquals(mockEmInfo.getCardMerged(), testingCard);
            Assert.assertEquals(COMPANY_ID, mockCardData.getCompanyId());
            Assert.assertNotNull(mockCardData.getToken());

            this.provider.deleteToken(testingCard, em);

            Assert.assertNull(mockCardData.getToken());
        }
        catch (CreditCardException e) {
            Assert.fail();
        }
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