package com.magenta.sc.localization;

public final class EchoLocalization {

    public static String getLocalized(final String bundleName, final String key) {
        return getResourceBundle(bundleName).getLocalized(key);
    }

    private static Localized getResourceBundle(final String bundleName) {
        return CommonResourceBundleProvider.getInstance().getBundle(bundleName);
    }


}