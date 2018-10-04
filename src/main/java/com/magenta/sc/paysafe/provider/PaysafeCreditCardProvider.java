package com.magenta.sc.paysafe.provider;

import com.magenta.sc.core.entity.booking.CreditCardTransaction;
import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.credit_cards.CreditCardProvider;
import com.magenta.sc.exception.CreditCardException;
import com.magenta.sc.paysafe.client.config.PaysafeClientConfig;
import com.magenta.sc.paysafe.error.PaysafeExceptionParser;
import com.magenta.sc.paysafe.helpers.HolderNameInfo;
import com.magenta.sc.paysafe.helpers.MerchantRefNumber;
import com.magenta.sc.paysafe.provider.verification.CardVerificationParameters;
import com.magenta.sc.paysafe.provider.verification.CardValidationRequestBuilder;
import com.paysafe.PaysafeApiClient;
import com.paysafe.cardpayments.*;
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

    private boolean isValidCard(CreditCard card, Long companyId, EntityManager em, boolean checkCvv, boolean checkPostcode) throws CreditCardException {
        boolean isSuccess = false;

        logger.info("Card x%s: card verification requested.", card.getLast4Digits());

        String merchantRefNum = MerchantRefNumber.generate();

        CardVerificationParameters parameters = new CardVerificationParameters(
                card,
                merchantRefNum,
                checkCvv,
                checkPostcode
        );

        try {

            Verification verification = CardValidationRequestBuilder.build(parameters);

            /**
             * Paysafe verification API call.
             * @see https://developer.paysafe.com/en/cards/api/#/introduction/complex-json-objects/verifications
             */
            Verification verificationResponse = client.cardPaymentService().verify(verification);

            isSuccess = verificationResponse.getStatus() == Status.COMPLETED;

            if (checkCvv &&
                verificationResponse.getCvvVerification() != CvvVerification.MATCH) {
                logger.error("Card x%s: Cvv verification failed.", card.getLast4Digits());
                isSuccess = false;
            }

            if (checkCvv && checkPostcode &&
                verificationResponse.getAvsResponse() != AvsResponse.MATCH &&
                verificationResponse.getAvsResponse() != AvsResponse.MATCH_ZIP_ONLY) {
                logger.error("Card x%s: Postcode verification failed.", card.getLast4Digits());
                isSuccess = false;
            }

        } catch (PaysafeException ev) {
            if (!PaysafeExceptionParser.isValidationError(ev)) {
                if (ev.getCode().equals("3004")) {// The zip/postal code must be provided for an AVS check request.
                    logger.error("Card x%s: Neither card nor default post was provider for paysafe gateway", card.getLast4Digits());
                    throw new CreditCardException(CreditCardException.EMPTY_POSTCODE);
                }
                throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);
            }
            isSuccess = false;
        } catch (IOException e) {
            logger.error("Card x%s: Verification failed due to IO error.", card.getLast4Digits());
            throw new CreditCardException(CreditCardException.CREDIT_CARD_PROVIDER_NOT_AVAILABLE);
        }

        return isSuccess;
    }

    @Override
    public boolean isValidCard(CreditCard card, Long companyId, EntityManager em) throws CreditCardException {
        boolean res = isValidCard(card, companyId, em, false, false);
        if (res) {
            logger.info("Card x%s: card company Id updated.", card.getLast4Digits());
            card.setCompanyId(companyId);
            em.merge(card);
        }
        return res;
    }

    @Override
    public Pair<CreditCard, Collection<CreditCardTransaction>> registerCreditCard(
            CreditCard card,
            Long companyId,
            EntityManager em,
            boolean checkCvvAndPostcode,
            boolean checkPostcode) throws CreditCardException {

        if (checkCvvAndPostcode) {
            if (!isValidCard(card, companyId, em, true, checkPostcode)) {
                logger.error("Can't register card %s, card validation failed.",
                             card.getLast4Digits());
                throw new CreditCardException(CreditCardException.INVALID_CARD_INFO);
            }
        }

        boolean isSuccess = false;
        String token;

        try {

            HolderNameInfo holder = HolderNameInfo.fromString(card.getHolderName());

            Profile profile = Profile.builder()
                .merchantCustomerId(java.util.UUID.randomUUID().toString())
                .locale(Locale.EN_US)
                .firstName(holder.getFirstName())
                .lastName(holder.getLastName())
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
