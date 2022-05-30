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
    private final boolean closeBrowserTabOnSuccess;

    private CallbackServerConfig(Builder builder) {
        callbackListener = builder.callbackListener;
        successRedirectUri = builder.successRedirectUri;
        closeBrowserTabOnSuccess = builder.closeBrowserTabOnSuccess;
    }

    public CallbackListener getCallbackListener() {
        return callbackListener;
    }

    public Optional<String> getSuccessRedirectUri() {
        return Optional.ofNullable(successRedirectUri);
    }

    public boolean isCloseBrowserTabOnSuccess() {
        return closeBrowserTabOnSuccess;
    }

    @Override
    public String toString() {
        return "CallbackServerConfig{" + "callbackListener=" + callbackListener + ", successRedirectUri='" +
                successRedirectUri + '\'' + ", closeBrowserTabOnSuccess=" + closeBrowserTabOnSuccess + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CallbackServerConfig)) {
            return false;
        }
        CallbackServerConfig config = (CallbackServerConfig) o;
        return closeBrowserTabOnSuccess == config.closeBrowserTabOnSuccess &&
                Objects.equals(callbackListener, config.callbackListener) &&
                Objects.equals(successRedirectUri, config.successRedirectUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callbackListener, successRedirectUri, closeBrowserTabOnSuccess);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private CallbackListener callbackListener;
        private String successRedirectUri;
        private boolean closeBrowserTabOnSuccess;

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

        public Builder withCloseBrowserTabOnSuccess(boolean val) {
            closeBrowserTabOnSuccess = val;
            return this;
        }

        public CallbackServerConfig build() {
            return new CallbackServerConfig(this);
        }
    }
}
