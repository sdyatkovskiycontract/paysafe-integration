package com.magenta.sc.paysafe.client.config;

import com.magenta.sc.paysafe.provider.PaysafeCreditCardProvider;
import com.paysafe.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PaysafeClientConfigFactory {

    private static PaysafeClientConfigFactory instance;

    private final Logger log = LoggerFactory.getLogger(PaysafeCreditCardProvider.class);

    private static final String TEST_ENVIRONMENT = "test";
    private static final String LIVE_ENVIRONMENT = "live";

    private PaysafeClientConfigFactory() {}

    public PaysafeClientConfig fromProperties(String fileName)
            throws PaysafeClientConfigException
    {

        String apiKey;
        String apiPassword;
        String accountNumber;
        String currencyCode;
        String currencyMultiplier;
        Environment environment;

        try {
            Properties props = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceStream = loader.getResourceAsStream(fileName);
            props.load(resourceStream);

            apiKey = props.getProperty("paysafeApiKeyId");
            apiPassword = props.getProperty("paysafeApiKeySecret");
            accountNumber = props.getProperty("paysafeAccountNumber");
            currencyCode = props.getProperty("currencyCode");
            currencyMultiplier = props.getProperty("currencyBaseUnitsMultiplier");
            String environmentStr = props.getProperty("endpointType");

            switch (environmentStr) {
                case TEST_ENVIRONMENT:
                    environment = Environment.TEST;
                    break;
                case LIVE_ENVIRONMENT:
                    environment = Environment.LIVE;
                    break;
                default:
                    log.error("Invalid endpoint type. Allowed values: '%s', '%s'.",
                            TEST_ENVIRONMENT,
                            LIVE_ENVIRONMENT);
                    throw new PaysafeClientConfigException("Invalid endpoint type.");
            }

        } catch (IOException ex) {
            log.error("Unable to load config properties", ex);
            throw new PaysafeClientConfigException("Unable to load config properties.", ex);
        }

        return new PaysafeClientConfig(
                apiKey,
                apiPassword,
                accountNumber,
                currencyCode,
                Integer.parseInt(currencyMultiplier),
                environment
        );
    }

    public static synchronized PaysafeClientConfigFactory getInstance() {
        if (instance == null) {
            instance = new PaysafeClientConfigFactory();
        }
        return instance;
    }

}
