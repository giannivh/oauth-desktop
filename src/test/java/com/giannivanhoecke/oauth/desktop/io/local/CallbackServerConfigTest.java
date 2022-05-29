package com.giannivanhoecke.oauth.desktop.io.local;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class CallbackServerConfigTest {

    private static final CallbackListener CALLBACK_LISTENER = authorizationCodeResponse -> {};
    private static final String SUCCESS_REDIRECT_URI        = "http://localhost/success.html";

    @Test
    public void builderWithSuccessRedirectUri() {
        // when
        CallbackServerConfig config = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(SUCCESS_REDIRECT_URI)
                .build();

        // then
        assertThat(config.getCallbackListener(), is(equalTo(CALLBACK_LISTENER)));
        assertThat(config.getSuccessRedirectUri(), is(not(equalTo(null))));
        assertThat(config.getSuccessRedirectUri().isPresent(), is(true));
        assertThat(config.getSuccessRedirectUri().get(), is(equalTo(SUCCESS_REDIRECT_URI)));
    }

    @Test
    public void builderWithoutSuccessRedirectUri() {
        // when
        CallbackServerConfig config = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(null)
                .build();

        // then
        assertThat(config.getCallbackListener(), is(equalTo(CALLBACK_LISTENER)));
        assertThat(config.getSuccessRedirectUri(), is(not(equalTo(null))));
        assertThat(config.getSuccessRedirectUri().isPresent(), is(false));
    }

    @Test
    public void equalsTrueWithSuccessRedirectUri() {
        // given
        CallbackServerConfig thisConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(SUCCESS_REDIRECT_URI)
                .build();
        CallbackServerConfig otherConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(SUCCESS_REDIRECT_URI)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(true));
        assertThat(equalsOther, is(true));
    }

    @Test
    public void equalsTrueWithoutSuccessRedirectUri() {
        // given
        CallbackServerConfig thisConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(null)
                .build();
        CallbackServerConfig otherConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(null)
                .build();

        // when
        boolean equalsThis = thisConfig.equals(otherConfig);
        boolean equalsOther = otherConfig.equals(thisConfig);

        // then
        assertThat(equalsThis, is(true));
        assertThat(equalsOther, is(true));
    }

    @Test
    public void equalsFalseCallbackListener() {
        // given
        CallbackServerConfig thisConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(SUCCESS_REDIRECT_URI)
                .build();
        CallbackServerConfig otherConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(authorizationCodeResponse -> {})
                .withSuccessRedirectUri(SUCCESS_REDIRECT_URI)
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
        CallbackServerConfig thisConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(SUCCESS_REDIRECT_URI)
                .build();
        CallbackServerConfig otherConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri("other-success-redirect-uri")
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
        CallbackServerConfig thisConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(SUCCESS_REDIRECT_URI)
                .build();
        CallbackServerConfig otherConfig = CallbackServerConfig
                .newBuilder()
                .withCallbackListener(CALLBACK_LISTENER)
                .withSuccessRedirectUri(SUCCESS_REDIRECT_URI)
                .build();

        // when
        int thisHashCode = thisConfig.hashCode();
        int otherHashCode = otherConfig.hashCode();

        // then
        assertThat(thisHashCode, is(equalTo(otherHashCode)));
    }
}
