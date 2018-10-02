package com.magenta.sc.core.entity.booking;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.exception.CreditCardException;
import org.joda.time.DateTime;

public interface CreditCardTransaction extends PaymentTransaction {
    // getStatus is defined in PaymentTransaction - a nasty interface inheritance stuff
    void setStatus(CreditCardTransactionStatus status);

    Integer getErrorCode();
    void setErrorCode(Integer errorCode);

    String getErrorMessage();
    void setErrorMessage(String errorMessage);

    Integer getTxID();
    void setTxID(Integer txID);

    Integer getTxLocID();
    void setTxLocID(Integer txLocID);

    String getTxRefGUID();
    void setTxRefGUID(String txRefGUID);

    /**
     * [messageNo] - varchar(512)
     */
    String getMessageNo();
    /**
     * [messageNo] - varchar(512)
     */
    void setMessageNo(String messageNo);

    Integer getAuthStatus();
    void setAuthStatus(Integer authStatus);

    String getAuthCode();
    void setAuthCode(String authCode);

    String getTransactionRef();
    void setTransactionRef(String transactionRef);

    String getResponseMsg();
    void setResponseMsg(String responseMsg);

    DateTime getTxDate();
    void setTxDate(DateTime txDate);

    String getMerchantNo();
    void setMerchantNo(String merchantNo);

    Double getAmount();
    void setAmount(Double amount);

    String getAmountText();

    String getCurrency();
    void setCurrency(String currency);

    String getMaskedCardNo();
    void setMaskedCardNo(String maskedCardNo);

    Integer getProcessStatus();
    void setProcessStatus(Integer processStatus);

    Integer getProcessSeconds();
    void setProcessSeconds(Integer processSeconds);

    DateTime getSettlementDate();
    void setSettlementDate(DateTime settlementDate);

    CreditCard getCard();
    void setCard(CreditCard card);

    //-- Business methods --//

    String getAccountName();

    String getAccountNumber();

    String getContactName();

    String getLog();

    Object[] toCSVArray();

    String[] getCSVFields();

    String getMessage();

    String getCompanyName();

    void setReconciliationID(String reconciliationID);
    String getReconciliationID();

    void setCreditCardException(CreditCardException cce);

    /**
     * @see <a href="https://www-test.adflex.co.uk/adflex-documentation.aspx?cat=gen&prod=keyconcepts&id=10&type=chapter">Adflex doc</a>
     * Some Adflex APIs give you the opportunity to test the issuers risk response of the authorised transactions CVC and Address verification enabling you to accept or reject the transaction.
     * If you do not want to accept the risk and reject the transaction, Adflex will attempt a reversal to cancel the transaction.
     *
     *  First Digit CVC                 - 1: Not Checked, 2: Match, 4: Not Matched, 8: Reserved
     *  Second Digit Postcode Numerics  - 1: Not Checked, 2: Match, 4: Not Matched, 8: Partial Match
     *  Third Digit Address Numerics    - 1: Not Checked, 2: Match, 4: Not Matched, 8: Partial Match
     *  Fourth Digit Authorising Entity - 1: Card Acceptor, 2: Acquirer, 4: Card Scheme, 8: Card Issuer
     *
     * @return Card AVS/CVC Status Response
     */
    String getCscAvsResponse();
    void setCscAvsResponse(String cscAvsResponse);
}