package com.magenta.sc.credit_cards;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.exception.CreditCardException;
import javafx.util.Pair;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public interface CreditCardProvider {

    /**
     * This method checks the validity of the card
     *
     * @param card          credit card for validation
     * @param companyId     id of company for credit card
     * @param em            EntityManager for execute query
     * @return result of validation
     * @throws CreditCardException
     */
    boolean isValidCard(CreditCard card,
                        Long companyId,
                        EntityManager em) throws CreditCardException;

    /**
     * This method tried register credit card
     *
     * @param card                  credit card for registration
     * @param companyId             id of company for credit card
     * @param em                    EntityManager for execute query
     * @param checkCvvAndPostcode   if true check cvv and check postcode when checkPostcode = true
     * @param checkPostcode         account.isCheckPostcodeForFrontEndBookings()
     * @return credit card with sets token, level, motoSupport flag and collection of transactions which were completed for validation
     * @throws CreditCardException
     */
    Pair<CreditCard, Collection<CreditCardTransaction>> registerCreditCard(CreditCard card,
                                                                           Long companyId,
                                                                           EntityManager em,
                                                                           boolean checkCvvAndPostcode,
                                                                           boolean checkPostcode) throws CreditCardException;

    /**
     * This method tries settle transaction with txRefGUID
     *
     * @param card          credit card for getting security settings
     * @param txRefGUID     transaction reference GUID for settle operation
     * @param amount        amount of money for settle operation
     * @param invoiceNumber number of invoice
     * @param em            EntityManager for execute query
     * @return settle transaction in success case or null if settle of transaction was failed
     */
    CreditCardTransaction settleTransaction(CreditCard card,
                                            String txRefGUID,
                                            Double amount,
                                            String invoiceNumber,
                                            EntityManager em) throws CreditCardException;

    /**
     * This method locks money on the credit card
     *
     * @param additionalAmount amount money for lock
     * @param card             credit card for locking money
     * @param transactionRef   id of locking transaction
     * @param jobId            id of job for payment
     * @param em               EntityManager for execute query
     * @return Credit card transaction, result of locking
     * @throws CreditCardException
     */
    CreditCardTransaction lockAdditionalAmount(Double additionalAmount,
                                               CreditCard card,
                                               String transactionRef,
                                               Long jobId,
                                               EntityManager em) throws CreditCardException;

    /**
     * This method locks money on the credit card
     *
     * @param csc            Card security code
     * @param amount         amount money for lock
     * @param card           credit card for locking money
     * @param transactionRef id of locking transaction
     * @param jobId          id of job for payment
     * @param em             EntityManager for execute query
     * @return Credit card transaction, result of locking
     * @throws CreditCardException
     */
    CreditCardTransaction lockPayment(String csc,
                                      Double amount,
                                      CreditCard card,
                                      String transactionRef,
                                      Long jobId,
                                      EntityManager em) throws CreditCardException;

    boolean deleteToken(CreditCard card,
                        EntityManager em) throws CreditCardException;

    /**
     * This method authorizes card by token and submits payment
     *
     * @param csc            Card security code
     * @param transactionRef id of transaction
     * @param amount         amount money for submit
     * @param card           credit card for submit
     * @param jobId          id of job for submit
     * @param invoiceId      invoice id for submit
     * @param em             EntityManager for execute query
     * @return Credit card transaction, result of authorize and submit
     * @throws CreditCardException
     */
    CreditCardTransaction authoriseAndSubmitByToken(String csc,
                                                    String transactionRef,
                                                    Double amount,
                                                    CreditCard card,
                                                    Long jobId,
                                                    Long invoiceId,
                                                    EntityManager em) throws CreditCardException;

    /**
     * This method authorizes card by token and refunds payment
     *
     * @param csc            Card security code
     * @param transactionRef id of transaction
     * @param amount         amount money for refund
     * @param card           credit card for refund
     * @param jobId          id of job for refund
     * @param invoiceId      invoice id for refund
     * @param em             EntityManager for execute query
     * @return Credit card transaction, result of authorize and refund
     * @throws CreditCardException
     */
    CreditCardTransaction authoriseAndRefundByToken(String csc,
                                                    String transactionRef,
                                                    Double amount,
                                                    CreditCard card,
                                                    Long jobId,
                                                    Long invoiceId,
                                                    EntityManager em) throws CreditCardException;

    /**
     * This method written only for developers, it export transactions to file on the server,
     *
     * @param start         start date as string for report period
     * @param end           end date for report period(inclusive)
     * @param companyId     id of company for transactions
     * @param em            EntityManager for execute query
     * @return list of data
     * @throws CreditCardException
     */
    List<String[]> getTransactions(String start,
                                   String end,
                                   Long companyId,
                                   EntityManager em) throws CreditCardException;

    /**
     * Returns count of days when authorized transaction can be submitted
     *
     * @return count of days
     */
    int getMaxAuthorisedDays();

    /**
     * Check transaction error code
     *
     * @param transaction
     * @return boolean - true, if transaction is correct
     */
    boolean isCorrectTransaction(CreditCardTransaction transaction);


}
