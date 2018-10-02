package com.magenta.sc.paysafe.client.config;

import com.paysafe.Environment;

public class PaysafeClientConfig {

    private final String apiKey;
    private final String apiPassword;
    private final String accountNumber;
    private final String currencyCode;
    private final int currencyMultiplier;
    private Environment environment;

    public PaysafeClientConfig(String apiKey,
                               String apiPassword,
                               String accountNumber,
                               String currencyCode,
                               int currencyMultiplier,
                               Environment environment) {
        this.apiKey = apiKey;
        this.apiPassword = apiPassword;
        this.accountNumber = accountNumber;
        this.currencyCode = currencyCode;
        this.currencyMultiplier = currencyMultiplier;
        this.environment = environment;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public int getCurrencyMultiplier() {
        return currencyMultiplier;
    }

    public Environment getApiEndPoint() {
        return this.environment;
    }
}
