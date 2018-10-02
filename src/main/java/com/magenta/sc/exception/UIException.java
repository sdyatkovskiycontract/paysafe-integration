package com.magenta.sc.exception;

import java.util.Arrays;


public class UIException extends Exception {
    private String messageKey;
    private Object[] messageParams;

    public UIException() {
    }

    public UIException(final String messageKey) {
        this.messageKey = messageKey;
    }

    public UIException(final String messageKey, final Object... messageParams) {
        this.messageKey = messageKey;
        this.messageParams = (messageParams == null) ? null : Arrays.copyOf(messageParams, messageParams.length);
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageParams() {
        return messageParams;
    }

    public String getMessageParam(int idx) {
        return messageParams != null && messageParams.length > idx ? messageParams[idx].toString() : "";
    }

}