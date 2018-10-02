package com.magenta.sc.core.entity.booking;

import com.magenta.sc.common.intf.Identified;
import com.magenta.sc.exception.TypeException;

public interface PaymentTransaction extends Identified {
    boolean isFailed();

    void throwTransactionException() throws Exception;

    CreditCardTransactionStatus getStatus();

    TypeException getException();

    /**
     * Used for Tag <InvoiceTotal> in notifications
     */
    String getTotalText();
}