package com.magenta.sc.paysafe.error;

import com.magenta.sc.exception.CreditCardException;
import com.paysafe.common.PaysafeException;

import java.io.IOException;

public class CreditCardExceptionFactory {
    public static CreditCardException whenFailedToValidateCard(PaysafeException ev) {
        return new CreditCardException(
                CreditCardException.INVALID_CARD_INFO,
                "Failed to settle transaction");
    }

    public static CreditCardException fromIOException(IOException ev) {
        return new CreditCardException(
                CreditCardException.CREDIT_CARD_PROVIDER_NOT_AVAILABLE,
                "Failed to settle transaction");
    }
}
