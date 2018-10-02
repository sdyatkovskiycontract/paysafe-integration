package com.magenta.sc.localization;

import com.magenta.sc.EchoConstants;
import com.magenta.sc.EchoUtils;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class CommonResourceBundleProvider {

    private static final Object lock = new Object();

    private static final String DEFAULT_BUNDLE_NAME = EchoConstants.DEFAULT_MESSAGES;
    private static final String AUTO = "auto";

    private static HashMap<Pair<Locale, String>, UniversalResourceBundle> resourceBundles = new HashMap<>();

    private static CommonResourceBundleProvider instance;

    private static String localeLanguage;
    private static String localeCountry;

    public static CommonResourceBundleProvider getInstance() {
        if (instance == null) {
            synchronized (lock) {
                try {
                    instance = new CommonResourceBundleProvider();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return instance;
    }

    private CommonResourceBundleProvider() throws IOException {
        resourceBundles = new HashMap<>();
    }

    public UniversalResourceBundle getBundle(Locale locale, String bundleName, String companyCode) {
        UniversalResourceBundle resourceBundle;
        try {
            resourceBundle = getUniversalBundle(locale, bundleName, companyCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resourceBundle;
    }

    private UniversalResourceBundle getUniversalBundle(Locale locale, String bundleName, String companyCode) throws IOException {
        synchronized (lock) {
            String bName = (bundleName == null) ? DEFAULT_BUNDLE_NAME : bundleName;
            locale = correctLocaleIfNeed(locale);

            Pair<Locale, String> bundleKey = new Pair<>(locale, bName);
            final UniversalResourceBundle bundle = resourceBundles.get(bundleKey);
            if (bundle != null && !bundle.isModified()) {
                return bundle;
            }

            UniversalResourceBundle rb = new UniversalResourceBundle(
                    bName,
                    "", //Bundle name
                    "application",// + FILE_SEPARATOR + INTERNATIONALIZATION,
                    locale,
                    companyCode);
            resourceBundles.put(bundleKey, rb);
            return rb;
        }
    }

    @NotNull
    private Locale correctLocaleIfNeed(Locale locale) {
        final Locale localeDefault = (!EchoUtils.isEmpty(localeLanguage) && !EchoUtils.isEmpty(localeCountry))
                ? new Locale(localeLanguage, localeCountry) : Locale.getDefault();
        if (locale == null) {
            locale = localeDefault;
        } else {
            boolean recreate = false;
            String language = locale.getLanguage();
            String country = locale.getCountry();

            if (EchoUtils.isEmpty(language) || language.compareToIgnoreCase(AUTO) == 0) {
                language = localeDefault.getLanguage();
                recreate = true;
            }
            if (EchoUtils.isEmpty(country) || country.compareToIgnoreCase(AUTO) == 0) {
                country = localeDefault.getCountry();
                recreate = true;
            }

            if (recreate) {
                locale = new Locale(language, country);
            }
        }
        return locale;
    }

    public UniversalResourceBundle getBundle(String bundleName) {
        UniversalResourceBundle resourceBundle;
        try {
            resourceBundle = getUniversalBundle(null, bundleName, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resourceBundle;
    }

    public Locale getDefaultLocale() {
        String language = localeLanguage, country = localeCountry;

        final Locale localeDefault = Locale.getDefault();
        final String languageDefault = localeDefault.getLanguage();
        final String countryDefault = localeDefault.getCountry();

        if (localeLanguage == null || localeLanguage.compareToIgnoreCase(AUTO) == 0) {
            language = languageDefault;
        }
        if (localeCountry == null || localeCountry.compareToIgnoreCase(AUTO) == 0) {
            country = countryDefault;
        }

        return new Locale(language, country);
    }

    public synchronized static void setLocale(String language, String country) {
        localeLanguage = language;
        localeCountry = country;
        resourceBundles.clear();
    }
}