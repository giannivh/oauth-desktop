package com.giannivanhoecke.oauth.desktop.io.local;

import java.util.Objects;
import java.util.Optional;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class CallbackServerConfig {

    private final CallbackListener callbackListener;
    private final String successRedirectUri;

    private CallbackServerConfig(Builder builder) {
        callbackListener = builder.callbackListener;
        successRedirectUri = builder.successRedirectUri;
    }

    public CallbackListener getCallbackListener() {
        return callbackListener;
    }

    public Optional<String> getSuccessRedirectUri() {
        return Optional.ofNullable(successRedirectUri);
    }

    @Override
    public String toString() {
        return "CallbackServerConfig{" + "callbackListener=" + callbackListener + ", successRedirectUri='" +
                successRedirectUri + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CallbackServerConfig)) {
            return false;
        }
        CallbackServerConfig that = (CallbackServerConfig) o;
        return Objects.equals(callbackListener, that.callbackListener) &&
                Objects.equals(successRedirectUri, that.successRedirectUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callbackListener, successRedirectUri);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private CallbackListener callbackListener;
        private String successRedirectUri;

        private Builder() {
        }

        public Builder withCallbackListener(CallbackListener val) {
            callbackListener = val;
            return this;
        }

        public Builder withSuccessRedirectUri(String val) {
            successRedirectUri = val;
            return this;
        }

        public CallbackServerConfig build() {
            return new CallbackServerConfig(this);
        }
    }
}
