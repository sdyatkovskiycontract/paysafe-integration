package com.magenta.sc.exception;


public class CreditCardException extends TypeException {

    public static final int CREDIT_CARD_PROVIDER_NOT_AVAILABLE = 1;
    public static final int INVALID_CARD_INFO = 2;
    public static final int NOT_REGISTERED_CARD = 3;
    public static final int NO_MERCHANT_ID_DEFINED = 4;
    public static final int INVALID_TRANSACTION_INFO = 5;
    public static final int REFUND_IS_NOT_AVAILABLE_NOW = 6;
    public static final int REFUND_IS_NOT_COMPLETED = 7;
    public static final int WRONG_REPORT_DATE_RANGE = 8;
    public static final int FUNDS_NOT_SUFFICIENT = 9;
    public static final int EMPTY_POSTCODE = 10;
    public static final int EMPTY_SECURE_CODE = 11;
    public static final int NO_STRIPE_SECRET_KEY_DEFINED = 12;
    public static final int NOT_SUPPORTED_CARD_TYPE = 13;
    public static final int FAILED_TO_CHECK_RECEIPT = 14;

    private final Exception exception;

    public CreditCardException(final int type, final Object... messageParams) {
        this(type, null, messageParams);
    }

    public CreditCardException(final int type, final Exception e, final Object... messageParams) {
        super(type, messageParams);
        this.exception = e;
    }

    @Override
    public String getMessageKey() {
        final int type = getType();
        switch (type) {
            case CREDIT_CARD_PROVIDER_NOT_AVAILABLE:
                return "credit.card.provider.unavailable";

            case INVALID_CARD_INFO:
                return "credit.card.provider.invalid";

            case NOT_REGISTERED_CARD:
                return "credit.card.provider.not_registered";

            case NO_MERCHANT_ID_DEFINED:
                return "credit.card.provider.merchant_id_not_defined";

            case REFUND_IS_NOT_AVAILABLE_NOW:
                return "credit.card.provider.refund_is_not_available_now";

            case REFUND_IS_NOT_COMPLETED:
                return "credit.card.provider.refund_is_not_completed";

            case WRONG_REPORT_DATE_RANGE:
                return "credit.card.provider.wrong_report_date_range";

            case INVALID_TRANSACTION_INFO:
                return "credit.card.provider.invalid_transaction";

            case FUNDS_NOT_SUFFICIENT:
                return "credit.card.provider.funds_not_sufficient";

            case EMPTY_POSTCODE:
                return "credit.card.provider.empty_postcode";

            case EMPTY_SECURE_CODE:
                return "credit.card.provider.empty_csc";

            case NO_STRIPE_SECRET_KEY_DEFINED:
                return "credit.card.provider.stripe_secret_key_defined";

            case NOT_SUPPORTED_CARD_TYPE:
                return "credit.card.provider.type_not_supported";

            case FAILED_TO_CHECK_RECEIPT:
                return "credit.card.provider.failed_to_check_receipt";

            default:
                throw new IllegalStateException("Unknown error type: " + type);
        }
    }

    public Exception getException() {
        return (exception == null) ? this : exception;
    }
}