package com.magenta.sc.exception;

import com.magenta.sc.EchoConstants;
import com.magenta.sc.localization.EchoLocalization;

public class TypeException extends UIException {

    private final int type;

    public TypeException(int type) {
        this.type = type;
    }

    public TypeException(int type, Object... messageParams) {
        super(null, messageParams);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public String getMessage() {
        try {
            String key = getMessageKey();
            return String.format(EchoLocalization.getLocalized(getLocalizedBundle(), key), getMessageParams());
        } catch (Exception ex) {
            return "";
        }
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    protected String getLocalizedBundle() {
        return EchoConstants.DIALOG_MESSAGES;
    }
}