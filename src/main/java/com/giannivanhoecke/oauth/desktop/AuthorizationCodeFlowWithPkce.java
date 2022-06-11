package com.giannivanhoecke.oauth.desktop;

import com.giannivanhoecke.oauth.desktop.exception.ActionNotSupportedException;
import com.giannivanhoecke.oauth.desktop.exception.BrowserException;
import com.giannivanhoecke.oauth.desktop.exception.CallbackServerException;
import com.giannivanhoecke.oauth.desktop.exception.IllegalStateException;
import com.giannivanhoecke.oauth.desktop.exception.PkceException;
import com.giannivanhoecke.oauth.desktop.exception.TokenException;
import com.giannivanhoecke.oauth.desktop.exception.UserInfoException;
import com.giannivanhoecke.oauth.desktop.exception.VerificationException;
import com.giannivanhoecke.oauth.desktop.extension.Pkce;
import com.giannivanhoecke.oauth.desktop.io.HttpStatusCode;
import com.giannivanhoecke.oauth.desktop.io.local.CallbackServer;
import com.giannivanhoecke.oauth.desktop.io.local.CallbackServerConfig;
import com.giannivanhoecke.oauth.desktop.io.remote.GetRequest;
import com.giannivanhoecke.oauth.desktop.io.remote.GetResult;
import com.giannivanhoecke.oauth.desktop.io.remote.KeyValuePair;
import com.giannivanhoecke.oauth.desktop.io.remote.PostRequest;
import com.giannivanhoecke.oauth.desktop.io.remote.PostResult;
import com.giannivanhoecke.oauth.desktop.io.remote.RemoteResource;
import com.giannivanhoecke.oauth.desktop.io.remote.RequestParameters;
import com.giannivanhoecke.oauth.desktop.io.remote.ResourceException;
import com.giannivanhoecke.oauth.desktop.representation.AccessTokenResponse;
import com.giannivanhoecke.oauth.desktop.representation.UserInfoResponse;
import com.giannivanhoecke.oauth.desktop.representation.internal.AuthorizationCodeResponse;
import com.giannivanhoecke.oauth.desktop.representation.internal.GrantType;
import com.giannivanhoecke.oauth.desktop.system.Browser;
import com.giannivanhoecke.oauth.desktop.system.DefaultBrowser;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class AuthorizationCodeFlowWithPkce {

    private final AuthorizationServerConfig authorizationServerConfig;
    private final CallbackServer callbackServer;
    private final Browser browser;

    private Pkce pkce;
    private String state;
    private String redirectUri;
    private CompletableFuture<AccessTokenResponse> accessTokenResponseCompletableFuture;

    /**
     * Creates an instance of the Authorization Code Flow with PKCE, using the system's default browser.
     *
     * @param authorizationServerConfig the authorization server config
     */
    public AuthorizationCodeFlowWithPkce(AuthorizationServerConfig authorizationServerConfig) {
        this(authorizationServerConfig, new DefaultBrowser());
    }

    /**
     * Creates an instance of the Authorization Code Flow with PKCE using a custom browser.
     *
     * @param authorizationServerConfig the authorization server config
     * @param browser the browser to use
     */
    public AuthorizationCodeFlowWithPkce(AuthorizationServerConfig authorizationServerConfig, Browser browser) {
        this.authorizationServerConfig = authorizationServerConfig;
        this.browser = browser;
        this.callbackServer = new CallbackServer(CallbackServerConfig
                .newBuilder()
                .withCallbackListener(this::callbackReceived)
                .withSuccessRedirectUri(this.authorizationServerConfig.getSuccessRedirectUri())
                .build());
    }

    /**
     * Starts the authorization flow. This will open a new browser tab redirecting to your authorization server.
     * A background server is started which will catch the authorization server's redirect to get the authorization
     * code. After shutting down the background server, and exchanging the authorization code for tokens, the
     * {@link Future} will complete, or will throw a {@link TokenException} or {@link VerificationException} on failure.
     *
     * @return the {@link AccessTokenResponse} as a {@link Future}
     *
     * @throws IllegalStateException when an authorization is already ongoing
     * @throws PkceException when the PKCE code couldn't be generated
     * @throws ActionNotSupportedException when {@link java.awt.Desktop} is not supported on the current platform
     * @throws BrowserException when the browser couldn't be opened
     * @throws CallbackServerException when the callback server couldn't be started
     */
    public Future<AccessTokenResponse> authorize() {
        assertCanStartAuthorization();
        initAuthorization();
        String authzUrl = this.authorizationServerConfig.getEndpointAuth() +
                "?response_type=code" +
                "&code_challenge=" + pkce.getCodeChallenge() +
                "&code_challenge_method=" + Pkce.CODE_CHALLENGE_METHOD +
                "&client_id=" + this.authorizationServerConfig.getClientId() +
                "&redirect_uri=" + redirectUri +
                "&scope=" + uriEncode(this.authorizationServerConfig.getAuthScope()) +
                "&state=" + state;
        this.browser.open(authzUrl);
        return accessTokenResponseCompletableFuture;
    }

    /**
     * Exchanges a refresh token for a new access token and a new refresh token.
     *
     * @param refreshToken the refresh token to use
     * @return a new {@link AccessTokenResponse}
     *
     * @throws TokenException when the refresh action failed
     */
    public AccessTokenResponse refresh(String refreshToken) {
        return requestTokens(GrantType.REFRESH_TOKEN, refreshToken);
    }

    /**
     * Gets the user info by access token.
     *
     * @param accessToken the access token to use
     * @return A {@link UserInfoResponse}
     *
     * @throws UserInfoException when the user info request failed
     */
    public UserInfoResponse getUserInfo(String accessToken) {
        KeyValuePair token = KeyValuePair.authorizationBearerFor(accessToken);
        GetRequest getRequest = new GetRequest(RequestParameters
                .newBuilder()
                .withEndpoint(this.authorizationServerConfig.getEndpointUserInfo())
                .withKeyValuePairs(token)
                .build());
        GetResult getResult = sendUserInfoGetRequest(getRequest);
        assertValidUserInfoResponse(getResult);
        return toUserInfoResponse(getResult);
    }
    
    // util

    private void assertCanStartAuthorization() {
        if (this.accessTokenResponseCompletableFuture != null && !this.accessTokenResponseCompletableFuture.isDone()) {
            throw new IllegalStateException("An authorization request is already ongoing");
        }
    }

    private void assertValidAuthorizationState(String authorizationState) {
        if (!state.equals(authorizationState)) {
            accessTokenResponseCompletableFuture.completeExceptionally(new VerificationException("Invalid state"));
        }
    }

    private void initAuthorization() {
        accessTokenResponseCompletableFuture = new CompletableFuture<>();
        pkce = Pkce.generate();
        state = UUID.randomUUID().toString();
        try {
            callbackServer.start();
            redirectUri = uriEncode(this.callbackServer.getSuccessEndpoint());
        } catch (Exception e) {
            throw new CallbackServerException("Cannot start callback server", e);
        }
    }

    private void callbackReceived(AuthorizationCodeResponse authorizationCodeResponse) {
        callbackServer.stop();
        exchangeAuthorizationCode(authorizationCodeResponse);
    }

    private void exchangeAuthorizationCode(AuthorizationCodeResponse authorizationCodeResponse) {
        assertValidAuthorizationState(authorizationCodeResponse.getState());
        try {
            AccessTokenResponse accessTokenResponse = requestTokens(
                    GrantType.AUTHORIZATION_CODE, authorizationCodeResponse.getCode());
            accessTokenResponseCompletableFuture.complete(accessTokenResponse);
        } catch (Exception e) {
            accessTokenResponseCompletableFuture.completeExceptionally(e);
        }
    }

    private AccessTokenResponse requestTokens(GrantType grantType, String code) {
        String payload = buildRequestTokensPayload(grantType, code);
        PostRequest postRequest = new PostRequest(RequestParameters
                .newBuilder()
                .withEndpoint(this.authorizationServerConfig.getEndpointToken())
                .withKeyValuePairs(KeyValuePair.CONTENT_TYPE_FORM)
                .build(), payload);
        PostResult postResult = sendTokenPostRequest(postRequest);
        assertValidTokenResponse(postResult);
        return toAccessTokenResponse(postResult);
    }

    private String buildRequestTokensPayload(GrantType grantType, String code) {
        StringBuilder payload = new StringBuilder()
                .append("grant_type=").append(grantType.name().toLowerCase())
                .append("&client_id=").append(this.authorizationServerConfig.getClientId());
        if (grantType == GrantType.AUTHORIZATION_CODE) {
            payload
                    .append("&code_verifier=").append(pkce.getCodeVerifier())
                    .append("&code=").append(code)
                    .append("&redirect_uri=").append(redirectUri);
        } else if (grantType == GrantType.REFRESH_TOKEN) {
            payload
                    .append("&refresh_token=").append(code);
        }
        return payload.toString();
    }

    private PostResult sendTokenPostRequest(PostRequest postRequest) {
        try {
            return new RemoteResource().post(postRequest);
        } catch (ResourceException e) {
            throw new TokenException(String.format("Cannot request tokens: %s", e.getMessage()), e);
        }
    }

    private void assertValidTokenResponse(PostResult postResult) {
        if (postResult == null) {
            throw new TokenException("Something went wrong while exchanging token");
        }
        if (postResult.getResultCode() != HttpStatusCode.OK) {
            throw new TokenException(String.format("Unauthorized: %s", postResult));
        }
    }

    private AccessTokenResponse toAccessTokenResponse(PostResult postResult) {
        try {
            return new Gson().fromJson(postResult.getContent(), AccessTokenResponse.class);
        } catch (Exception e) {
            throw new TokenException(String.format("Cannot create AccessTokenResponse: %s", postResult), e);
        }
    }

    private GetResult sendUserInfoGetRequest(GetRequest getRequest) {
        try {
            return new RemoteResource().get(getRequest);
        } catch (ResourceException e) {
            throw new UserInfoException(String.format("Cannot request user info: %s", e.getMessage()), e);
        }
    }

    private void assertValidUserInfoResponse(GetResult getResult) {
        if (getResult == null) {
            throw new UserInfoException("Something went wrong while getting user info");
        }
        if (getResult.getResultCode() != HttpStatusCode.OK) {
            throw new UserInfoException(String.format("Unauthorized: %s", getResult));
        }
    }

    private UserInfoResponse toUserInfoResponse(GetResult getResult) {
        try {
            return new Gson().fromJson(getResult.getContent(), UserInfoResponse.class);
        } catch (Exception e) {
            throw new UserInfoException(String.format("Cannot create UserInfoResponse: %s", getResult), e);
        }
    }
    
    private String uriEncode(String toEncode) {
        return URLEncoder.encode(toEncode, StandardCharsets.UTF_8);
    }
}
