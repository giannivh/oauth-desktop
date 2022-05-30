package com.giannivanhoecke.oauth.desktop;

import com.giannivanhoecke.oauth.desktop.exception.MissingConfigException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class AuthorizationServerConfig {

    public static final String DEFAULT_ENDPOINT_AUTH      = "/auth";
    public static final String DEFAULT_ENDPOINT_USER_INFO = "/userinfo";
    public static final String DEFAULT_ENDPOINT_TOKEN     = "/token";
    public static final String DEFAULT_AUTH_SCOPE         = "openid offline_access email profile";
    
    private final String baseUrl;
    private final String endpointAuth;
    private final String endpointUserInfo;
    private final String endpointToken;
    private final String clientId;
    private final String authScope;
    private final String successRedirectUri;
    private final boolean closeBrowserTabOnSuccess;

    private AuthorizationServerConfig(Builder builder) {
        baseUrl = builder.baseUrl;
        endpointAuth = builder.endpointAuth;
        endpointUserInfo = builder.endpointUserInfo;
        endpointToken = builder.endpointToken;
        clientId = builder.clientId;
        authScope = builder.authScope;
        successRedirectUri = builder.successRedirectUri;
        closeBrowserTabOnSuccess = builder.closeBrowserTabOnSuccess;
    }

    public String getEndpointAuth() {
        return String.format("%s%s", baseUrl, endpointAuth);
    }

    public String getEndpointUserInfo() {
        return String.format("%s%s", baseUrl, endpointUserInfo);
    }

    public String getEndpointToken() {
        return String.format("%s%s", baseUrl, endpointToken);
    }

    public String getClientId() {
        return clientId;
    }

    public String getAuthScope() {
        return authScope;
    }

    public String getSuccessRedirectUri() {
        return successRedirectUri;
    }

    public boolean isCloseBrowserTabOnSuccess() {
        return closeBrowserTabOnSuccess;
    }

    @Override
    public String toString() {
        return "AuthorizationServerConfig{" + "baseUrl='" + baseUrl + '\'' + ", endpointAuth='" + endpointAuth + '\'' +
                ", endpointUserInfo='" + endpointUserInfo + '\'' + ", endpointToken='" + endpointToken + '\'' +
                ", clientId='" + clientId + '\'' + ", authScope='" + authScope + '\'' + ", successRedirectUri='" +
                successRedirectUri + '\'' + ", closeBrowserTabOnSuccess=" + closeBrowserTabOnSuccess + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorizationServerConfig)) {
            return false;
        }
        AuthorizationServerConfig config = (AuthorizationServerConfig) o;
        return closeBrowserTabOnSuccess == config.closeBrowserTabOnSuccess && Objects.equals(baseUrl, config.baseUrl) &&
                Objects.equals(endpointAuth, config.endpointAuth) &&
                Objects.equals(endpointUserInfo, config.endpointUserInfo) &&
                Objects.equals(endpointToken, config.endpointToken) && Objects.equals(clientId, config.clientId) &&
                Objects.equals(authScope, config.authScope) &&
                Objects.equals(successRedirectUri, config.successRedirectUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseUrl, endpointAuth, endpointUserInfo, endpointToken, clientId, authScope,
                successRedirectUri, closeBrowserTabOnSuccess);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String baseUrl;
        private String endpointAuth;
        private String endpointUserInfo;
        private String endpointToken;
        private String clientId;
        private String authScope;
        private String successRedirectUri;
        private boolean closeBrowserTabOnSuccess;

        private Builder() {
            endpointAuth = DEFAULT_ENDPOINT_AUTH;
            endpointUserInfo = DEFAULT_ENDPOINT_USER_INFO;
            endpointToken = DEFAULT_ENDPOINT_TOKEN;
            authScope = DEFAULT_AUTH_SCOPE;
            closeBrowserTabOnSuccess = false;
        }

        /**
         * Sets the base URL of your authorization server.
         *
         * @param val the base URL to use
         * @return this builder for chaining
         */
        public Builder withBaseUrl(String val) {
            baseUrl = val;
            return this;
        }

        /**
         * Sets your authorization endpoint. Make sure you leave out the base URL.
         * Defaults to `/auth`.
         *
         * @param val the endpoint to use
         * @return this builder for chaining
         */
        public Builder withEndpointAuth(String val) {
            endpointAuth = val;
            return this;
        }

        /**
         * Sets your user info endpoint. Make sure you leave out the base URL.
         * Defaults to `/userinfo`.
         *
         * @param val the endpoint to use
         * @return this builder for chaining
         */
        public Builder withEndpointUserInfo(String val) {
            endpointUserInfo = val;
            return this;
        }

        /**
         * Sets your token exchange endpoint. Make sure you leave out the base URL.
         * Defaults to `/token`.
         *
         * @param val the endpoint to use
         * @return this builder for chaining
         */
        public Builder withEndpointToken(String val) {
            endpointToken = val;
            return this;
        }

        /**
         * Sets your client ID.
         *
         * @param val the client ID to use
         * @return this builder for chaining
         */
        public Builder withClientId(String val) {
            clientId = val;
            return this;
        }

        /**
         * Sets your authorization scope. Use a space separated String.
         * Defaults to `openid offline_access email profile`.
         *
         * @param val the endpoint to use
         * @return this builder for chaining
         */
        public Builder withAuthScope(String val) {
            authScope = val;
            return this;
        }

        /**
         * Optional redirect URI when authorization was successful.
         * Leave empty to use the built-in success page.
         * If {@link #withCloseBrowserTabOnSuccess(boolean)} is set to true, this property is ignored.
         *
         * @param val the redirect URI to use
         * @return this builder for chaining
         */
        public Builder withSuccessRedirectUri(String val) {
            successRedirectUri = val;
            return this;
        }

        /**
         * When set to true, the browser tab for authorization will close automatically on success.
         * Defaults to false.
         * Note that this property has precedence over your own redirect URI set by
         * {@link #withSuccessRedirectUri(String)}.
         *
         * @param val whether to close the browser tab on success or not
         * @return this builder for chaining
         */
        public Builder withCloseBrowserTabOnSuccess(boolean val) {
            closeBrowserTabOnSuccess = val;
            return this;
        }

        /**
         * Constructs the {@link AuthorizationServerConfig} object with your given parameters.
         *
         * @return the {@link AuthorizationServerConfig} object
         *
         * @throws MissingConfigException when mandatory fields are missing
         */
        public AuthorizationServerConfig build() {
            if (StringUtils.isBlank(baseUrl)) {
                throw new MissingConfigException("baseUrl cannot be null or empty");
            }
            if (StringUtils.isBlank(endpointAuth)) {
                throw new MissingConfigException("endpointAuth cannot be null or empty");
            }
            if (StringUtils.isBlank(endpointUserInfo)) {
                throw new MissingConfigException("endpointUserInfo cannot be null or empty");
            }
            if (StringUtils.isBlank(endpointToken)) {
                throw new MissingConfigException("endpointToken cannot be null or empty");
            }
            if (StringUtils.isBlank(clientId)) {
                throw new MissingConfigException("clientId cannot be null or empty");
            }
            if (StringUtils.isBlank(authScope)) {
                throw new MissingConfigException("authScope cannot be null or empty");
            }
            return new AuthorizationServerConfig(this);
        }
    }
}
