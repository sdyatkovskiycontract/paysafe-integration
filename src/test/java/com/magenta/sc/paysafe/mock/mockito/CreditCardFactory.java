package com.magenta.sc.paysafe.mock.mockito;

import com.magenta.sc.core.entity.customer.CreditCard;
import com.magenta.sc.paysafe.mock.data.MockCardData;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreditCardFactory {

    public static final String CSV_SEPARATOR = ";";
    public static final int CSV_HOLDER_IDX = 0;
    public static final int CSV_NUMBER_IDX = 1;
    public static final int CSV_SECURE_CODE_IDX = 2;
    public static final int CSV_COMPANY_ID_IDX = 3;
    public static final int CSV_EXPIRED_FLAG_IDX = 4;

    public static final String CSV_EXPIRED_VALUE = "Expired";
    public static final String CSV_NOT_EXPIRED_VALUE = "Not expired";

    private static void setupData(CreditCard card, MockCardData data) {
        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            Long id = (Long)args[0];
            data.setCompanyId(id);
            return null;
        }).when(card).setCompanyId(anyLong());
    }

    @NotNull
    public static CreditCard getCard(String holderName,
                                     String number,
                                     String secureCode,
                                     String companyId,
                                     Integer expiryMonth,
                                     Integer expiryYear,
                                     MockCardData data) {
        CreditCard card = mock(CreditCard.class);

        when(card.getHolderName()).thenReturn(holderName);
        when(card.getNumber()).thenReturn(number);


        DateTime expire = new DateTime(expiryYear, expiryMonth, 1, 0, 0);
        when(card.getExpireDate()).thenReturn(expire);
        when(card.getSecureCode()).thenReturn(secureCode);

        Long companyIdLong = Long.parseLong(companyId);
        when(card.getCompanyId()).thenReturn(companyIdLong);

        setupData(card, data);

        return card;
    }

    @NotNull
    public static CreditCard getNonExpiredCard(String holderName,
                                               String number,
                                               String secureCode,
                                               String companyId,
                                               MockCardData data) {
        DateTime expire = LocalDateTime.now().plusMonths(1).toDateTime();

        return getCard(holderName,
                number,
                secureCode,
                companyId,
                expire.getMonthOfYear(),
                expire.getYear(),
                data);
    }

    @NotNull
    public static CreditCard getExpiredCard(String holderName,
                                            String number,
                                            String secureCode,
                                            String companyId,
                                            MockCardData data) {
        DateTime expire = LocalDateTime.now().minusMonths(1).toDateTime();

        return getCard(holderName,
                number,
                secureCode,
                companyId,
                expire.getMonthOfYear(),
                expire.getYear(),
                data);
    }

    @NotNull
    public static CreditCard fromCSV(String csv) throws IllegalArgumentException {

        String[] csvParts = csv.split(CSV_SEPARATOR);

        if (csvParts.length != 5)
            throw new IllegalArgumentException("CSV string has wrong format");

        if (csvParts[CSV_EXPIRED_FLAG_IDX].trim().equals(CSV_EXPIRED_VALUE))
            return getExpiredCard(csvParts[CSV_HOLDER_IDX].trim(),
                                  csvParts[CSV_NUMBER_IDX].trim(),
                                  csvParts[CSV_COMPANY_ID_IDX].trim(),
                                  csvParts[CSV_SECURE_CODE_IDX].trim(),
                                  null);

        return getNonExpiredCard(csvParts[CSV_HOLDER_IDX].trim(),
                                 csvParts[CSV_NUMBER_IDX].trim(),
                                 csvParts[CSV_COMPANY_ID_IDX].trim(),
                                 csvParts[CSV_SECURE_CODE_IDX].trim(),
                                 null);
    }

    @NotNull
    public static CreditCard fromCSV(String csv, MockCardData data) throws IllegalArgumentException {

        String[] csvParts = csv.split(CSV_SEPARATOR);

        if (csvParts.length != 5)
            throw new IllegalArgumentException("CSV string has wrong format");

        if (csvParts[CSV_EXPIRED_FLAG_IDX].trim().equals(CSV_EXPIRED_VALUE))
            return getExpiredCard(csvParts[CSV_HOLDER_IDX].trim(),
                    csvParts[CSV_NUMBER_IDX].trim(),
                    csvParts[CSV_COMPANY_ID_IDX].trim(),
                    csvParts[CSV_SECURE_CODE_IDX].trim(),
                    data);

        return getNonExpiredCard(csvParts[CSV_HOLDER_IDX].trim(),
                csvParts[CSV_NUMBER_IDX].trim(),
                csvParts[CSV_COMPANY_ID_IDX].trim(),
                csvParts[CSV_SECURE_CODE_IDX].trim(),
                data);
    }
}
