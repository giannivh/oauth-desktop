package com.giannivanhoecke.oauth.desktop;

import com.giannivanhoecke.oauth.desktop.exception.TokenException;
import com.giannivanhoecke.oauth.desktop.exception.UserInfoException;
import com.giannivanhoecke.oauth.desktop.exception.VerificationException;
import com.giannivanhoecke.oauth.desktop.fixtures.AuthorizationServerMock;
import com.giannivanhoecke.oauth.desktop.fixtures.BrowserMock;
import com.giannivanhoecke.oauth.desktop.representation.AccessTokenResponse;
import com.giannivanhoecke.oauth.desktop.representation.UserInfoResponse;
import com.giannivanhoecke.oauth.desktop.system.Browser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorizationCodeFlowWithPkceTest {

    private AuthorizationServerMock authorizationServerMock;
    private AuthorizationServerConfig authorizationServerConfig;

    @BeforeAll
    public void setup()
            throws IOException {
        authorizationServerMock = new AuthorizationServerMock();
        authorizationServerMock.start();
        
        authorizationServerConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(authorizationServerMock.getBaseUrl())
                .withClientId(AuthorizationServerMock.VALID_CLIENT_ID)
                .build();
    }

    @AfterAll
    public void teardown() {
        authorizationServerMock.stop();
    }

    @BeforeEach
    public void reset() {
        authorizationServerMock.resetAuth();
    }

    @Test
    public void authorize()
            throws ExecutionException, InterruptedException, TimeoutException {
        // given
        Browser browser = new BrowserMock();
        AuthorizationCodeFlowWithPkce flow = new AuthorizationCodeFlowWithPkce(authorizationServerConfig, browser);

        // when
        Future<AccessTokenResponse> accessTokenResponseFuture = flow.authorize();
        AccessTokenResponse accessTokenResponse = accessTokenResponseFuture.get(1, TimeUnit.MINUTES);

        // then
        assertThat(accessTokenResponse, is(not(equalTo(null))));
        assertThat(accessTokenResponse.getAccessToken(), is(equalTo(AuthorizationServerMock.VALID_ACCESS_TOKEN)));
        assertThat(accessTokenResponse.getRefreshToken(), is(equalTo(AuthorizationServerMock.VALID_REFRESH_TOKEN)));
        assertThat(accessTokenResponse.getExpiresIn(), is(equalTo(300)));
    }

    @Test
    public void authorizeInvalidStateThrowsVerificationException() {
        // given
        this.authorizationServerMock.overrideAuthState("invalid-auth-state");
        Browser browser = new BrowserMock();
        AuthorizationCodeFlowWithPkce flow = new AuthorizationCodeFlowWithPkce(authorizationServerConfig, browser);

        // when
        Future<AccessTokenResponse> accessTokenResponseFuture = flow.authorize();

        // expect
        ExecutionException executionException = assertThrows(
                ExecutionException.class, () -> accessTokenResponseFuture.get(1, TimeUnit.MINUTES));

        // then
        assertThat(executionException, is(not(equalTo(null))));
        assertThat(executionException.getCause(), is(instanceOf(VerificationException.class)));
        assertThat(executionException.getCause().getMessage(), is(equalTo("Invalid state")));
    }

    @Test
    public void authorizeInvalidCodeThrowsTokenException() {
        // given
        this.authorizationServerMock.overrideAuthCode("invalid-auth-code");
        Browser browser = new BrowserMock();
        AuthorizationCodeFlowWithPkce flow = new AuthorizationCodeFlowWithPkce(authorizationServerConfig, browser);

        // when
        Future<AccessTokenResponse> accessTokenResponseFuture = flow.authorize();

        // expect
        ExecutionException executionException = assertThrows(
                ExecutionException.class, () -> accessTokenResponseFuture.get(1, TimeUnit.MINUTES));

        // then
        assertThat(executionException, is(not(equalTo(null))));
        assertThat(executionException.getCause(), is(instanceOf(TokenException.class)));
        assertThat(executionException.getCause().getMessage(), is(
                equalTo("Unauthorized: PostResult{resultCode=401, resultBody='Unauthorized'}")));
    }

    @Test
    public void getUserInfo() {
        // given
        String accessToken = AuthorizationServerMock.VALID_ACCESS_TOKEN;
        AuthorizationCodeFlowWithPkce flow = new AuthorizationCodeFlowWithPkce(authorizationServerConfig);

        // when
        UserInfoResponse userInfoResponse = flow.getUserInfo(accessToken);

        // then
        assertThat(userInfoResponse, is(not(equalTo(null))));
        assertThat(userInfoResponse.getId(), is(equalTo("9047370c-24f9-45f1-9959-faca5e60e6b9")));
        assertThat(userInfoResponse.getUsername(), is(equalTo("gianni@giannivanhoecke.com")));
        assertThat(userInfoResponse.getName(), is(equalTo("Gianni Van Hoecke")));
        assertThat(userInfoResponse.getEmail(), is(equalTo("gianni@giannivanhoecke.com")));
    }

    @Test
    public void getUserInfoInvalidTokenThrowsUserInfoException() {
        // given
        String accessToken = "invalid-access-token";
        AuthorizationCodeFlowWithPkce flow = new AuthorizationCodeFlowWithPkce(authorizationServerConfig);

        // expect when
        UserInfoException userInfoException = assertThrows(
                UserInfoException.class, () -> flow.getUserInfo(accessToken));

        // then
        assertThat(userInfoException, is(not(equalTo(null))));
        assertThat(userInfoException.getMessage(), is(
                equalTo("Unauthorized: GetResult{resultCode=401, content='Unauthorized'}")));
    }

    @Test
    public void refresh() {
        // given
        String refreshToken = AuthorizationServerMock.VALID_REFRESH_TOKEN;
        AuthorizationCodeFlowWithPkce flow = new AuthorizationCodeFlowWithPkce(authorizationServerConfig);

        // when
        AccessTokenResponse accessTokenResponse = flow.refresh(refreshToken);

        // then
        assertThat(accessTokenResponse, is(not(equalTo(null))));
        assertThat(accessTokenResponse.getAccessToken(), is(equalTo("new-access-token")));
        assertThat(accessTokenResponse.getRefreshToken(), is(equalTo("new-refresh-token")));
        assertThat(accessTokenResponse.getExpiresIn(), is(equalTo(300)));
    }

    @Test
    public void refreshInvalidTokenThrowsTokenException() {
        // given
        String refreshToken = "invalid-refresh-token";
        AuthorizationCodeFlowWithPkce flow = new AuthorizationCodeFlowWithPkce(authorizationServerConfig);

        // expect when
        TokenException tokenException = assertThrows(TokenException.class, () -> flow.refresh(refreshToken));

        // then
        assertThat(tokenException, is(not(equalTo(null))));
        assertThat(tokenException.getMessage(), is(
                equalTo("Unauthorized: PostResult{resultCode=401, resultBody='Unauthorized'}")));
    }
}
