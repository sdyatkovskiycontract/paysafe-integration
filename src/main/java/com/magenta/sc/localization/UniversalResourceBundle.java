package com.magenta.sc.localization;

import com.magenta.sc.EchoUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.ResourceBundleEnumeration;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class UniversalResourceBundle extends ResourceBundle implements Localized {

    private static final String XML_POSTFIX = ".xml";
    private static final String FILE_SEPARATOR = "/";

    private final Map<String, Object> commonLookup;
    private final Map<String, Object> customLookup1;
    private final Map<String, Object> customLookup2;
    private final Map<String, Object> baseCustomLookup;

    private final Logger log = LoggerFactory.getLogger(UniversalResourceBundle.class);

    public UniversalResourceBundle(final String baseName, final String generalName, final String storageFolder, final Locale locale) throws IOException {
        this(baseName, generalName, storageFolder, locale, null);
    }

    public UniversalResourceBundle(final String baseName, final String generalName, final String storageFolder, final Locale locale, final String companyCode) throws IOException {
        commonLookup = loadLookup(baseName, generalName, storageFolder, null, null);
        customLookup1 = loadLookup(baseName, generalName, storageFolder, null, companyCode);
        customLookup2 = loadLookup(baseName, generalName, storageFolder, locale, companyCode);
        baseCustomLookup = loadLookup(baseName, generalName, storageFolder, new Locale(locale.getLanguage(), locale.getLanguage()), companyCode);
    }

    private Map<String, Object> loadLookup(final String baseName, final String generalName, final String storageFolder, final Locale locale, final String companyCode) throws IOException {
        final Properties properties = new Properties();
        // Loading general properties
        addProperties(properties, generalName, storageFolder, locale);
        // Loading specific properties
        addProperties(properties, baseName, storageFolder, locale);

        if (!EchoUtils.isEmpty(companyCode)) {
            // Loading company specific properties
            addProperties(properties, baseName, storageFolder + FILE_SEPARATOR + companyCode, locale);
        }

        return new HashMap(properties);
    }

    private void addProperties(final Properties properties, final String name, final String storageFolder, final Locale locale) throws IOException {
        try (InputStream propertiesStream = getPropertiesFile(name, storageFolder, locale, XML_POSTFIX)) {
            if (propertiesStream != null) {
                properties.loadFromXML(propertiesStream);
            }
        }
    }

    @Override
    public String getLocalized(final String key) {
        try {
            return getString(key);
        }
        catch (final MissingResourceException e) {
//            if (key != null && !key.isEmpty()) {
//                log.error("Resource not found: " + key);
//            }
            return key;
        }
    }

    public boolean isModified() {
        return false;
    }

    @Override
    public Object handleGetObject(@NotNull final String key) {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }

        Object value = (customLookup2 == null) ? null : customLookup2.get(key);

        if (value == null && customLookup1 != null) {
            value = customLookup1.get(key);
        }

        if (value == null && baseCustomLookup != null) {
            value = baseCustomLookup.get(key);
        }

        return value != null ? value : commonLookup.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        final ResourceBundle parent = this.parent;
        final Enumeration<String> stringEnumeration = (parent != null) ? parent.getKeys() : null;
        return new ResourceBundleEnumeration(handleKeySet(), stringEnumeration);
    }

    @Override
    protected Set<String> handleKeySet() {
        final Set<String> keySet = commonLookup.keySet();

        if (customLookup1 != null) {
            keySet.addAll(customLookup1.keySet());
        }

        if (customLookup2 != null) {
            keySet.addAll(customLookup2.keySet());
        }

        if (baseCustomLookup != null) {
            keySet.addAll(baseCustomLookup.keySet());
        }

        return keySet;
    }

    private InputStream getPropertiesFile(final String name, final String storageFolder, final Locale locale, final String postfix) throws IOException {
        //        String fileSeparator = System.getProperty("file.separator");
        final StringBuilder sb = new StringBuilder();

        sb.append(storageFolder);
        sb.append(FILE_SEPARATOR);
        sb.append(name);

        if (locale != null) {
            final String localePrefix = String.format("_%s_%s", locale.getLanguage(), locale.getCountry());
            sb.append(localePrefix);
        }

        sb.append(postfix);
        final String pathname = sb.toString();

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        log.debug("pathname = " + pathname);
        InputStream stream = loader.getResourceAsStream(pathname);
        if (stream == null) {
            stream = getClass().getClassLoader().getResourceAsStream(pathname);
        }
        log.debug("url = " + loader.getResource(pathname));

        return stream;
    }
}