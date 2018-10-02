package com.magenta.sc.paysafe.provider;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.credit_cards.CreditCardProvider;
import com.magenta.sc.exception.CreditCardException;
import com.paysafe.PaysafeApiClient;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
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
    public CreditCardTransaction settleTransaction(CreditCard card, String txRefGUID, Double amount, String invoiceNumber, EntityManager em) throws CreditCardException {
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
