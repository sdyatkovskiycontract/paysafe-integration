package com.magenta.sc.core.entity.customer;

public enum CreditCardType {
    AMERICAN_EXPRESS(1, "American Express"),
    MASTERCARD(2, "Mastercard"),
    VISA_DELTA(4, "Visa/Visa Delta"),
    DISCOVER(5, "Discover"),
    DINERS_CLUB(6, "Diners Club"),
    JCB(7, "JCB"),
    CHIP_AND_PIN(8, "Chip and PIN");

    private int id;
    private String name;

    CreditCardType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static CreditCardType fromString(String representation) {
        if (representation == null) {
            return null;
        }
        representation = representation.trim();
        for (CreditCardType type : CreditCardType.values()) {
            if (type.name.equalsIgnoreCase(representation)) {
                return type;
            }
        }
        return null;
    }

    public static CreditCardType fromId(int id) {
        for (CreditCardType type : CreditCardType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
