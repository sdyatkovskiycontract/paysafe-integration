package com.magenta.sc.paysafe.provider;

public class PaysafeCreditCardProviderException extends Exception {
    public PaysafeCreditCardProviderException() {
    }

    public PaysafeCreditCardProviderException(String message) {
        super(message);
    }

    public PaysafeCreditCardProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaysafeCreditCardProviderException(Throwable cause) {
        super(cause);
    }

    public PaysafeCreditCardProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
