package com.magenta.sc.paysafe.provider;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.credit_cards.CreditCardProvider;
import com.magenta.sc.exception.CreditCardException;
import com.magenta.sc.paysafe.client.config.PaysafeClientConfig;
import com.magenta.sc.paysafe.error.PaysafeExceptionParser;
import com.magenta.sc.paysafe.helpers.HolderNameInfo;
import com.magenta.sc.paysafe.helpers.MerchantRefNumber;
import com.paysafe.PaysafeApiClient;
import com.paysafe.cardpayments.Authorization;
import com.paysafe.cardpayments.Status;
import com.paysafe.cardpayments.Verification;
import com.paysafe.common.Id;
import com.paysafe.common.Locale;
import com.paysafe.common.PaysafeException;
import com.paysafe.customervault.Profile;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PaysafeCreditCardProvider implements CreditCardProvider {

    private static final Logger logger = LoggerFactory.getLogger(PaysafeCreditCardProvider.class);

    private PaysafeApiClient client;
    private PaysafeClientConfig clientConfig;

    private static String PROFILE_TOKEN_SEPARATOR = ";";

    public PaysafeCreditCardProvider(PaysafeApiClient client,
                                     PaysafeClientConfig clientConfig) {
        this.client = client;
        this.clientConfig = clientConfig;
    }

    @Override
    public boolean isValidCard(CreditCard card, Long companyId, EntityManager em) throws CreditCardException {

        boolean isSuccess = false;

        String merchantRefNum = MerchantRefNumber.generate();

        try {

            String holderName = card.getHolderName();

            HolderNameInfo holder = HolderNameInfo.fromString(holderName);

            Verification verification = Verification.builder()
                .merchantRefNum(merchantRefNum)
                .card()
                    .cardNum(card.getNumber())
                    .cardExpiry()
                        .month(card.getExpireDate().getMonthOfYear())
                        .year(card.getExpireDate().getYearOfEra())
                    .done()
                .done()
                .profile()
                    .firstName(holder.getFirstName())
                    .lastName(holder.getLastName())
                    //.email("Joe.Smith@canada.com")
                .done()
                .billingDetails()
                    .zip(card.getPostcode())
                .done()
            .build();

            /**
             * Paysafe verification API call.
             * @see https://developer.paysafe.com/en/cards/api/#/introduction/complex-json-objects/verifications
             */
            Verification verificationResponse = client.cardPaymentService().verify(verification);

            isSuccess = verificationResponse.getStatus() == Status.COMPLETED;

        } catch (PaysafeException ev) {
            if (!PaysafeExceptionParser.isValidationError(ev)) {
                if (ev.getCode().equals("3004")) // The zip/postal code must be provided for an AVS check request.
                    throw new CreditCardException(CreditCardException.EMPTY_POSTCODE);
                throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);
            }
            isSuccess = false;
        } catch (IOException e) {
            throw new CreditCardException(CreditCardException.CREDIT_CARD_PROVIDER_NOT_AVAILABLE);
        }

        return isSuccess;
    }

    @Override
    public Pair<CreditCard, Collection<CreditCardTransaction>> registerCreditCard(
            CreditCard card,
            Long companyId,
            EntityManager em,
            boolean checkCvvAndPostcode,
            boolean checkPostcode) throws CreditCardException {

        // TODO: validate card, if checkCvvAndPostcode or checkPostcode are set.

        boolean isSuccess = false;
        String token = null;

        try {

            HolderNameInfo holder = HolderNameInfo.fromString(card.getHolderName());

            Profile profile = Profile.builder()
                .merchantCustomerId(java.util.UUID.randomUUID().toString())
                .locale(Locale.EN_US) // TODO: get rid of this field
                .firstName(holder.getFirstName())
                .lastName(holder.getLastName())
                .email("john.smith@somedomain.com")  // TODO: get rid of this field
                .phone("713-444-5555") // TODO: get rid of this field
                .build();

            Profile  profileRes = client.customerVaultService().create(profile);

            if (profileRes.getStatus() != com.paysafe.customervault.Status.ACTIVE) {
                // TODO: Add message, duplicate it into log.
                throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);
            }

            com.paysafe.customervault.Card createCardRequest =
                com.paysafe.customervault.Card.builder()
                    .profileId(profileRes.getId())
                    .cardNum(card.getNumber())
                    .cardExpiry()
                        .month(card.getExpireDate().getMonthOfYear())
                        .year(card.getExpireDate().getYearOfEra())
                    .done()
                .build();

            com.paysafe.customervault.Card createCardResponse = client.customerVaultService().create(createCardRequest);

            isSuccess = createCardResponse.getStatus() == com.paysafe.customervault.Status.ACTIVE;

            token = String.format("%s%s%s",
                    profileRes.getId(),
                    PROFILE_TOKEN_SEPARATOR,
                    createCardResponse.getPaymentToken());

        } catch (PaysafeException ev) {
            // TODO: Add message, duplicate it into log.
            throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);
        } catch (IOException e) {
            // TODO: Add message, duplicate it into log.
            throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);
        }

        if (!isSuccess)
            // TODO: Add message, duplicate it into log.
            throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);

        card.setCompanyId(companyId);
        card.setToken(token);

        em.merge(card);

        return new Pair<>(card, new ArrayList<>());
    }

    @Override
    public CreditCardTransaction settleTransaction(
            CreditCard card,
            String txRefGUID,
            Double amount,
            String invoiceNumber,
            EntityManager em) throws CreditCardException {

        boolean isSuccess = false;

        // TODO: get billing info
        String merchantRefNum = txRefGUID;
        String street = "some street";
        String city = "Super City";
        String state = "Some State";
        String country = "Country";
        String zip = "000000";

        // Few words about why "amount" should be integer.
        // From Paysafe developer site:
        // https://developer.paysafe.com/en/sdks/server-side/java/getting-started/
        //
        // Transactions are actually measured in fractions
        // of the currency specified in the currencyCode;
        // for example, USD transactions are measured in cents.
        // This multiplier is how many of these smaller units
        // make up one of the specified currency. For
        // example, with the currencyCode USD the value
        // is 100 but for Japanese YEN the multiplier would
        // be 1 as there is no smaller unit.

        Integer amountInt = (int) (amount * clientConfig.getCurrencyMultiplier());

        try {
            // Build our order object.
            Authorization auth
                    = Authorization.builder()
                    .merchantRefNum(merchantRefNum)
                    .amount(amountInt)
                    .settleWithAuth(true)
                    .billingDetails()
                    .street(street)
                    .city(city)
                    .state(state)
                    .country(country)
                    .zip(zip)
                    .done()
                    .card()
                    .cardNum(card.getNumber())
                    .cvv(card.getSecureCode())
                    .cardExpiry()
                    .month(card.getExpireDate().getMonthOfYear())
                    .year(card.getExpireDate().getYearOfCentury())
                    .done()
                    .done()
                    .build();

            Authorization authResponse = client.cardPaymentService().authorize(auth);

            isSuccess = true;

        } catch (PaysafeException ev) {
            throw new CreditCardException(
                    CreditCardException.INVALID_CARD_INFO,
                    "Failed to settle transaction");
        } catch (IOException e) {
            throw new CreditCardException(
                    CreditCardException.CREDIT_CARD_PROVIDER_NOT_AVAILABLE,
                    "Failed to settle transaction");        }

        return null;
    }

    @Override
    public CreditCardTransaction lockAdditionalAmount(Double additionalAmount, CreditCard card, String transactionRef, Long jobId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public CreditCardTransaction lockPayment(String csc, Double amount, CreditCard card, String transactionRef, Long jobId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public boolean deleteToken(CreditCard card, EntityManager em) throws CreditCardException {

        boolean isSuccess = false;
        String paymentToken = null;

        try {

            // TODO: get profileId by card Id
            String profileIdStr = null;
            Id<Profile> profileId =  Id.create(profileIdStr, Profile.class);

            // We assume, that each profile linked with only one card.
            // So, once we decide to delete token,
            // we just need to delete related profile.

            Profile profile = Profile.builder()
                    .id(profileId)
                    .build();

            isSuccess = client.customerVaultService().delete(profile);

        } catch (PaysafeException ev) {
            if (!PaysafeExceptionParser.isValidationError(ev))
                throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);
        } catch (IOException e) {
            throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);
        }

        // TODO: update database

        return isSuccess;
    }

    @Override
    public CreditCardTransaction authoriseAndSubmitByToken(String csc, String transactionRef, Double amount, CreditCard card, Long jobId, Long invoiceId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public CreditCardTransaction authoriseAndRefundByToken(String csc, String transactionRef, Double amount, CreditCard card, Long jobId, Long invoiceId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public List<String[]> getTransactions(String start, String end, Long companyId, EntityManager em) throws CreditCardException {
        return null;
    }

    @Override
    public int getMaxAuthorisedDays() {
        return 0;
    }

    @Override
    public boolean isCorrectTransaction(CreditCardTransaction transaction) {
        return false;
    }
}
