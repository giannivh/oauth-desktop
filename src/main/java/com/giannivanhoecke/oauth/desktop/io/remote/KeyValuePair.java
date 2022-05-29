package com.giannivanhoecke.oauth.desktop.io.remote;

import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class KeyValuePair {

    public static final KeyValuePair CONTENT_TYPE_FORM = KeyValuePair.of("Content-Type", "application/x-www-form-urlencoded");

    private static final String AUTHORIZATION_KEY                 = "Authorization";
    private static final String AUTHORIZATION_BEARER_VALUE_PREFIX = "Bearer";

    private final String key;
    private final String value;

    private KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        String value = this.value;
        if (key != null && key.equalsIgnoreCase(AUTHORIZATION_KEY)) {
            value = "[REDACTED]";
        }
        return "KeyValuePair{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeyValuePair)) {
            return false;
        }
        KeyValuePair that = (KeyValuePair) o;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    // util

    public static KeyValuePair of(String key, String value) {
        return new KeyValuePair(key, value);
    }

    public static KeyValuePair authorizationBearerFor(String token) {
        return new KeyValuePair(AUTHORIZATION_KEY, String.format("%s %s", AUTHORIZATION_BEARER_VALUE_PREFIX, token));
    }
}
