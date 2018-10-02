package com.magenta.sc.paysafe.provider;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.credit_cards.CreditCardProvider;
import com.magenta.sc.exception.CreditCardException;
import com.paysafe.Environment;
import com.paysafe.PaysafeApiClient;
import com.paysafe.cardpayments.Authorization;
import com.paysafe.common.Error;
import com.paysafe.common.PaysafeException;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class PaysafeCreditCardProvider implements CreditCardProvider {

    private static final String CONFIG_FILENAME = "config.properties";

    private final Logger log = LoggerFactory.getLogger(PaysafeCreditCardProvider.class);

    private PaysafeApiClient client;

    public PaysafeCreditCardProvider(PaysafeApiClient client) {
        this.client = client;
    }

    @Override
    public boolean isValidCard(CreditCard card, Long companyId, EntityManager em) throws CreditCardException {
        return false;
    }

    @Override
    public Pair<CreditCard, Collection<CreditCardTransaction>> registerCreditCard(CreditCard card, Long companyId, EntityManager em, boolean checkCvvAndPostcode, boolean checkPostcode) throws CreditCardException {
        return null;
    }

    @Override
    public CreditCardTransaction settleTransaction(
            CreditCard card,
            String txRefGUID,
            Double amount,
            String invoiceNumber,
            EntityManager em) throws CreditCardException {

        boolean isSuccess = false;

        // TODO: get billing info
        String merchantRefNum = txRefGUID;
        String street = "some street";
        String city = "Super City";
        String state = "Some State";
        String country = "Country";
        String zip = "000000";

        // Few words about why "amount" should be integer.
        // From Paysafe developer site:
        // https://developer.paysafe.com/en/sdks/server-side/java/getting-started/
        //
        // Transactions are actually measured in fractions
        // of the currency specified in the currencyCode;
        // for example, USD transactions are measured in cents.
        // This multiplier is how many of these smaller units
        // make up one of the specified currency. For
        // example, with the currencyCode USD the value
        // is 100 but for Japanese YEN the multiplier would
        // be 1 as there is no smaller unit.

        Integer amountInt = (int) (amount * 100);

        try {
            // Build our order object.
            Authorization auth
                    = Authorization.builder()
                    .merchantRefNum(merchantRefNum)
                    .amount(amountInt)
                    .settleWithAuth(true)
                    .billingDetails()
                    .street(street)
                    .city(city)
                    .state(state)
                    .country(country)
                    .zip(zip)
                    .done()
                    .card()
                    .cardNum(card.getNumber())
                    .cvv(card.getSecureCode())
                    .cardExpiry()
                    .month(card.getExpireDate().getMonthOfYear())
                    .year(card.getExpireDate().getYearOfCentury())
                    .done()
                    .done()
                    .build();

            Authorization authResponse = client.cardPaymentService().authorize(auth);

            isSuccess = true;

        } catch (PaysafeException ev) {
            throw new CreditCardException(
                    CreditCardException.INVALID_CARD_INFO,
                    "Failed to settle transaction");
        } catch (IOException e) {
            throw new CreditCardException(
                    CreditCardException.CREDIT_CARD_PROVIDER_NOT_AVAILABLE,
                    "Failed to settle transaction");        }

        return null;
    }

    @Override
    public CreditCardTransaction lockAdditionalAmount(Double additionalAmount, CreditCard card, String transactionRef, Long jobId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public CreditCardTransaction lockPayment(String csc, Double amount, CreditCard card, String transactionRef, Long jobId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public boolean deleteToken(CreditCard card, EntityManager em) throws CreditCardException {
        return false;
    }

    @Override
    public CreditCardTransaction authoriseAndSubmitByToken(String csc, String transactionRef, Double amount, CreditCard card, Long jobId, Long invoiceId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public CreditCardTransaction authoriseAndRefundByToken(String csc, String transactionRef, Double amount, CreditCard card, Long jobId, Long invoiceId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public List<String[]> getTransactions(String start, String end, Long companyId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public int getMaxAuthorisedDays() {
        return 0;
    }

    @Override
    public boolean isCorrectTransaction(CreditCardTransaction transaction) {
        return false;
    }
}
