package com.magenta.sc.paysafe.entity.transaction;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.booking.CreditCardTransactionStatus;
import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.exception.CreditCardException;
import com.magenta.sc.exception.TypeException;
import org.joda.time.DateTime;

public class CreditCardTransactionImpl implements CreditCardTransaction {

    private Double amount;
    private String currencyCode;
    private String maskedCardNo;
    private String merchantRefNum;
    private String authId;
    private String txnTime;
    private CreditCardTransactionStatus status;

    public CreditCardTransactionImpl(
            Double amount,
            String currencyCode,
            String maskedCardNo,
            String merchantRefNum,
            String authId,
            String txnTime,
            CreditCardTransactionStatus status) {
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.maskedCardNo = maskedCardNo;
        this.merchantRefNum = merchantRefNum;
        this.authId = authId;
        this.txnTime = txnTime;
        this.status = status;
    }

    @Override
    public void setStatus(CreditCardTransactionStatus status) {
        this.status = status;
    }

    @Override
    public Integer getErrorCode() {
        return null;
    }

    @Override
    public void setErrorCode(Integer errorCode) {

    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public void setErrorMessage(String errorMessage) {

    }

    @Override
    public Integer getTxID() {
        return null;
    }

    @Override
    public void setTxID(Integer txID) {

    }

    @Override
    public Integer getTxLocID() {
        return null;
    }

    @Override
    public void setTxLocID(Integer txLocID) {

    }

    @Override
    public String getTxRefGUID() {
        return this.merchantRefNum;
    }

    @Override
    public void setTxRefGUID(String txRefGUID) {
        this.merchantRefNum = txRefGUID;
    }

    @Override
    public String getMessageNo() {
        return null;
    }

    @Override
    public void setMessageNo(String messageNo) {

    }

    @Override
    public Integer getAuthStatus() {
        return null;
    }

    @Override
    public void setAuthStatus(Integer authStatus) {

    }

    @Override
    public String getAuthCode() {
        return null;
    }

    @Override
    public void setAuthCode(String authCode) {

    }

    @Override
    public String getTransactionRef() {
        return null;
    }

    @Override
    public void setTransactionRef(String transactionRef) {

    }

    @Override
    public String getResponseMsg() {
        return null;
    }

    @Override
    public void setResponseMsg(String responseMsg) {

    }

    @Override
    public DateTime getTxDate() {
        return null;
    }

    @Override
    public void setTxDate(DateTime txDate) {

    }

    @Override
    public String getMerchantNo() {
        return null;
    }

    @Override
    public void setMerchantNo(String merchantNo) {

    }

    @Override
    public Double getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(Double amount) {
        this.amount= amount;
    }

    @Override
    public String getAmountText() {
        return null;
    }

    @Override
    public String getCurrency() {
        return this.currencyCode;
    }

    @Override
    public void setCurrency(String currency) {
        this.currencyCode = currency;
    }

    @Override
    public String getMaskedCardNo() {
        return this.maskedCardNo;
    }

    @Override
    public void setMaskedCardNo(String maskedCardNo) {
        this.maskedCardNo = maskedCardNo;
    }

    @Override
    public Integer getProcessStatus() {
        return null;
    }

    @Override
    public void setProcessStatus(Integer processStatus) {

    }

    @Override
    public Integer getProcessSeconds() {
        return null;
    }

    @Override
    public void setProcessSeconds(Integer processSeconds) {

    }

    @Override
    public DateTime getSettlementDate() {
        return null;
    }

    @Override
    public void setSettlementDate(DateTime settlementDate) {

    }

    @Override
    public CreditCard getCard() {
        return null;
    }

    @Override
    public void setCard(CreditCard card) {

    }

    @Override
    public String getAccountName() {
        return null;
    }

    @Override
    public String getAccountNumber() {
        return null;
    }

    @Override
    public String getContactName() {
        return null;
    }

    @Override
    public String getLog() {
        return null;
    }

    @Override
    public Object[] toCSVArray() {
        return new Object[0];
    }

    @Override
    public String[] getCSVFields() {
        return new String[0];
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String getCompanyName() {
        return null;
    }

    @Override
    public void setReconciliationID(String reconciliationID) {

    }

    @Override
    public String getReconciliationID() {
        return null;
    }

    @Override
    public void setCreditCardException(CreditCardException cce) {

    }

    @Override
    public String getCscAvsResponse() {
        return null;
    }

    @Override
    public void setCscAvsResponse(String cscAvsResponse) {
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public void throwTransactionException() throws Exception {

    }

    @Override
    public CreditCardTransactionStatus getStatus() {
        return this.status;
    }

    @Override
    public TypeException getException() {
        return null;
    }

    @Override
    public String getTotalText() {
        return null;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }
}
