package com.magenta.sc.core.entity.booking;

public enum CreditCardTransactionStatus {
    AUTHORISED("Authorised"),
    INVALID("Invalid"),
    COMPLETED("Completed"),
    REFUNDED("Refunded"),
    @Deprecated //unused status
            SKIPPED("Skipped");

    private final String name;

    CreditCardTransactionStatus(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean transactionIsSuccessful(final CreditCardTransaction transaction) {
        if (transaction == null)
            throw new IllegalArgumentException("transaction cannot be null");

        return transactionIsSuccessful( transaction.getStatus() );
    }

    public static boolean transactionIsSuccessful(final CreditCardTransactionStatus status) {
        if (status == null)
            return false;

        switch (status) {
            case INVALID:
            case SKIPPED:
                return false;

            default:
                return true;
        }
    }
}