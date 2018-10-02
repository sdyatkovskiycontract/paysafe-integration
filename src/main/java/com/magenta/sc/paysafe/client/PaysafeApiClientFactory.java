package com.magenta.sc.paysafe.client;

import com.paysafe.PaysafeApiClient;
import com.magenta.sc.paysafe.client.config.PaysafeClientConfig;

public class PaysafeApiClientFactory {

    private static PaysafeApiClientFactory instance;

    public PaysafeApiClient createClient(PaysafeClientConfig config) {
        PaysafeApiClient apiClient = new PaysafeApiClient(
                config.getApiKey(),
                config.getApiPassword(),
                config.getApiEndPoint(),
                config.getAccountNumber());

        return apiClient;
    }

    public static PaysafeApiClientFactory getInstance() {
        if (instance == null) {
            instance = new PaysafeApiClientFactory();
        }
        return instance;
    }
}
