package com.magenta.sc.core.entity.customer;


import org.joda.time.DateTime;

public interface CreditCard {
    String getNumber();
    void setNumber(String number);

    /**
     * This attribute is transient
     *
     * @return cvv or cvs
     */
    String getSecureCode();
    /**
     * @param secureCode cvv or svs
     */
    void setSecureCode(String secureCode);

    CreditCardType getType();
    void setType(CreditCardType type);

    String getHolderName();
    void setHolderName(String holderName);

    DateTime getExpireDate();
    void setExpireDate(DateTime expireDate);
    String getExpireDateMonthYear();

    Contact getContact();
    void setContact(Contact contact);

    CustomerAccount getCustomerAccount();
    void setCustomerAccount(CustomerAccount customerAccount);

    Boolean isDefaultForBooking();
    void setDefaultForBooking(Boolean defaultForBooking);

    Boolean isDisabledForBooking();
    void setDisabledForBooking(Boolean disabledForBooking);

    String getToken();
    void setToken(String token);

    /**
     * @return whether {@link #getToken()} is not <code>null</code>
     */
    boolean isRegistered(); // transient

    Boolean isMotoSupported(); // transient
    void setMotoSupported(Boolean motoSupported); // transient

    String getLast4Digits();

    /**
     * <p>Method checks that card is enabled and not disabled for booking</p>
     * @return <code>true</code> if card is valid for booking and <code>false</code> otherwise.
     */
    boolean isValidForBooking();

    Boolean isTemporary();
    void setTemporary(Boolean temporary);

    String getBillingAddress();
    void setBillingAddress(String billingAddress);

    Long getCompanyId();
    void setCompanyId(Long companyId);

    /**
     * This Postcode uses for validating debit/credit cards through the card provider when we trying create new card.
     * This field is transient for hibernate
     *
     * @return postcode
     */
    String getPostcode();

    void setPostcode(String postcode);

    /**
     * Uses for created token after validate new card without saving. Token saved to temporary_tokens table by id = temporaryTokenId
     *
     * @return generatedId
     */
    Long getTemporaryTokenId();
    void setTemporaryTokenId(Long temporaryTokenId);

    /**
     * @return <code>true</code> &mdash; if individual credit card marked as Chip&Pin and uses only in Chip&Pin jobs
     * <br/>
     * <code>false</code> &mdash; otherwise
     */
    Boolean getChipAndPin();
    void setChipAndPin(Boolean chipAndPin);

    /**
     * @return card owner id in stripe
     */
    String getStripeCustomerId();
    void setStripeCustomerId(String stripeCustomerId);

    /**
     * Used for judopay card provider. Unique reference for owner card.
     */
    String getConsumerReference();
    void setConsumerReference(String consumerReference);

    DateTime getCreationDate();
    void setCreationDate(DateTime creationDate);
}