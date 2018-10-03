package com.magenta.sc.paysafe.helpers;

import com.magenta.sc.exception.CreditCardException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HolderNameInfo {
    private final String firstName;
    private final String lastName;

    private HolderNameInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Parses holder name. It should be in format '<FirstName> <LastName>'.
     * @param holderName holder name to be parsed
     * @return HolderNameInfo instance if successfull,
     * null holder name is of invalid format.
     */
    @NotNull
    public static HolderNameInfo fromString(String holderName) throws CreditCardException {
        String[] holderNames = holderName.split(" ");

        if (holderNames.length != 2)
            throw new CreditCardException(
                    CreditCardException.INVALID_CARD_INFO,
                    String.format(
                            "Holder name is invalid: %s, should be in " +
                                    "format '<FirstName> <LastName>'",
                            holderName)
            );

        String holderFirstName = holderNames[0].trim();
        String holderLastName = holderNames[1].trim();

        return new HolderNameInfo(holderFirstName, holderLastName);
    }
}
