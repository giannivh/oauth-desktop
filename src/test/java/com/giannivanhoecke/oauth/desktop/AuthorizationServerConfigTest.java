package com.giannivanhoecke.oauth.desktop;

import com.giannivanhoecke.oauth.desktop.exception.MissingConfigException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class AuthorizationServerConfigTest {

    private static final String BASE_URL  = "http://auth.server/auth/realms/my-realm/protocol/openid-connect";
    private static final String CLIENT_ID = "my-client-id";

    private static final String DEFAULT_ENDPOINT_AUTH      = "/auth";
    private static final String DEFAULT_ENDPOINT_USER_INFO = "/userinfo";
    private static final String DEFAULT_ENDPOINT_TOKEN     = "/token";
    private static final String DEFAULT_AUTH_SCOPE         = "openid offline_access email profile";
    private static final boolean DEFAULT_CLOSE_BROWSER_TAB = false;

    private static final String CUSTOM_ENDPOINT_AUTH        = "/custom/auth";
    private static final String CUSTOM_ENDPOINT_USER_INFO   = "/custom/userinfo";
    private static final String CUSTOM_ENDPOINT_TOKEN       = "/custom/token";
    private static final String CUSTOM_AUTH_SCOPE           = "offline_access openid";
    private static final String CUSTOM_SUCCESS_REDIRECT_URI = "http://localhost/success.html";
    private static final boolean CUSTOM_CLOSE_BROWSER_TAB   = true;

    @Test
    public void builderWithDefaultProperties() {
        // given
        String expectedEndpointAuth = String.format("%s%s", BASE_URL, DEFAULT_ENDPOINT_AUTH);
        String expectedEndpointUserInfo = String.format("%s%s", BASE_URL, DEFAULT_ENDPOINT_USER_INFO);
        String expectedEndpointToken = String.format("%s%s", BASE_URL, DEFAULT_ENDPOINT_TOKEN);

        // when
        AuthorizationServerConfig config = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .build();

        // then
        assertThat(config.getEndpointAuth(), is(equalTo(expectedEndpointAuth)));
        assertThat(config.getEndpointUserInfo(), is(equalTo(expectedEndpointUserInfo)));
        assertThat(config.getEndpointToken(), is(equalTo(expectedEndpointToken)));
        assertThat(config.getClientId(), is(equalTo(CLIENT_ID)));
        assertThat(config.getAuthScope(), is(equalTo(DEFAULT_AUTH_SCOPE)));
        assertThat(config.getSuccessRedirectUri(), is(equalTo(null)));
        assertThat(config.isCloseBrowserTabOnSuccess(), is(DEFAULT_CLOSE_BROWSER_TAB));
    }

    @Test
    public void builderWithCustomProperties() {
        // given
        String expectedEndpointAuth = String.format("%s%s", BASE_URL, CUSTOM_ENDPOINT_AUTH);
        String expectedEndpointUserInfo = String.format("%s%s", BASE_URL, CUSTOM_ENDPOINT_USER_INFO);
        String expectedEndpointToken = String.format("%s%s", BASE_URL, CUSTOM_ENDPOINT_TOKEN);

        // when
        AuthorizationServerConfig config = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(CUSTOM_CLOSE_BROWSER_TAB)
                .build();

        // then
        assertThat(config.getEndpointAuth(), is(equalTo(expectedEndpointAuth)));
        assertThat(config.getEndpointUserInfo(), is(equalTo(expectedEndpointUserInfo)));
        assertThat(config.getEndpointToken(), is(equalTo(expectedEndpointToken)));
        assertThat(config.getClientId(), is(equalTo(CLIENT_ID)));
        assertThat(config.getAuthScope(), is(equalTo(CUSTOM_AUTH_SCOPE)));
        assertThat(config.getSuccessRedirectUri(), is(equalTo(CUSTOM_SUCCESS_REDIRECT_URI)));
        assertThat(config.isCloseBrowserTabOnSuccess(), is(CUSTOM_CLOSE_BROWSER_TAB));
    }

    @Test
    public void equalsTrue() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(true));
        assertThat(equalsOther, is(true));
    }

    @Test
    public void equalsFalseBaseUrl() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl("other-base-url")
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseClientId() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId("other-client-id")
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseEndpointAuth() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth("other-endpoint-auth")
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseEndpointUserInfo() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo("other-endpoint-user-info")
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseEndpointToken() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken("other-endpoint-token")
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseAuthScope() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope("other-auth-scope")
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseSuccessRedirectUri() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri("other-success-redirect-uri")
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseCloseBrowserTabOnSuccess() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(CUSTOM_CLOSE_BROWSER_TAB)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsHashCode() {
        // given
        AuthorizationServerConfig thisConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();
        AuthorizationServerConfig otherConfig = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB)
                .build();

        // when
        int thisHashCode = thisConfig.hashCode();
        int otherHashCode = otherConfig.hashCode();

        // then
        assertThat(thisHashCode, is(equalTo(otherHashCode)));
    }

    @Test
    public void throwsMissingConfigExceptionBaseUrlNull() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(null)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("baseUrl cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionBaseUrlEmpty() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl("")
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("baseUrl cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionClientIdNull() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(null)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("clientId cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionClientIdEmpty() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId("")
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("clientId cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionEndpointAuthNull() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(null)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("endpointAuth cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionEndpointAuthEmpty() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth("")
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("endpointAuth cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionEndpointUserInfoNull() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(null)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("endpointUserInfo cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionEndpointUserInfoEmpty() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo("")
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("endpointUserInfo cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionEndpointTokenNull() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(null)
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("endpointToken cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionEndpointTokenEmpty() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken("")
                .withAuthScope(CUSTOM_AUTH_SCOPE)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("endpointToken cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionAuthScopeNull() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope(null)
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("authScope cannot be null or empty")));
    }

    @Test
    public void throwsMissingConfigExceptionAuthScopeEmpty() {
        // given
        AuthorizationServerConfig.Builder builder = AuthorizationServerConfig
                .newBuilder()
                .withBaseUrl(BASE_URL)
                .withClientId(CLIENT_ID)
                .withEndpointAuth(CUSTOM_ENDPOINT_AUTH)
                .withEndpointUserInfo(CUSTOM_ENDPOINT_USER_INFO)
                .withEndpointToken(CUSTOM_ENDPOINT_TOKEN)
                .withAuthScope("")
                .withSuccessRedirectUri(CUSTOM_SUCCESS_REDIRECT_URI)
                .withCloseBrowserTabOnSuccess(DEFAULT_CLOSE_BROWSER_TAB);

        // expect when
        MissingConfigException missingConfigException = assertThrows(MissingConfigException.class, builder::build);

        // then
        assertThat(missingConfigException.getMessage(), is(equalTo("authScope cannot be null or empty")));
    }
}
