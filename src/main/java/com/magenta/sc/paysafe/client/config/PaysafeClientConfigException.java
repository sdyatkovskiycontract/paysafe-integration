package com.magenta.sc.paysafe.client.config;

public class PaysafeClientConfigException extends Exception {
    public PaysafeClientConfigException() {
    }

    public PaysafeClientConfigException(String message) {
        super(message);
    }

    public PaysafeClientConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaysafeClientConfigException(Throwable cause) {
        super(cause);
    }

    public PaysafeClientConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
