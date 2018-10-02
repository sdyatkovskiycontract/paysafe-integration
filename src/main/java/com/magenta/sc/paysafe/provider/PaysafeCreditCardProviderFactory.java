package com.magenta.sc.paysafe.provider;

import com.paysafe.PaysafeApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.magenta.sc.paysafe.client.config.PaysafeClientConfig;
import com.magenta.sc.paysafe.client.config.PaysafeClientConfigFactory;
import com.magenta.sc.paysafe.client.config.PaysafeClientConfigException;
import com.magenta.sc.paysafe.client.PaysafeApiClientFactory;

public class PaysafeCreditCardProviderFactory {

    private static PaysafeCreditCardProviderFactory instance;

    private final Logger log = LoggerFactory.getLogger(PaysafeCreditCardProvider.class);

    public PaysafeCreditCardProvider create(String resourceName)
            throws PaysafeCreditCardProviderException
    {

        PaysafeClientConfig config;

        try {
            config = PaysafeClientConfigFactory
                    .getInstance()
                    .fromProperties(resourceName);
        } catch (PaysafeClientConfigException e) {
            String message = "Failed to load provider configuration";
            log.error(message, e);
            throw new PaysafeCreditCardProviderException(message, e);
        }

        PaysafeApiClient client = PaysafeApiClientFactory.getInstance().createClient(config);

        return new PaysafeCreditCardProvider(client);
    }

    public static synchronized PaysafeCreditCardProviderFactory getInstance() {
        if (instance == null) {
            instance = new PaysafeCreditCardProviderFactory();
        }
        return instance;
    }

}
