package com.magenta.sc.localization;

@FunctionalInterface
public interface Localized {
    String getLocalized(String key);
}