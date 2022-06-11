package com.giannivanhoecke.oauth.desktop.io.local;

import com.giannivanhoecke.oauth.desktop.io.remote.GetRequest;
import com.giannivanhoecke.oauth.desktop.io.remote.GetResult;
import com.giannivanhoecke.oauth.desktop.io.remote.RemoteResource;
import com.giannivanhoecke.oauth.desktop.io.remote.RequestParameters;
import com.giannivanhoecke.oauth.desktop.io.remote.ResourceException;
import com.giannivanhoecke.oauth.desktop.representation.internal.AuthorizationCodeResponse;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class CallbackServerTest {

    private final CallbackServer callbackServer;
    
    private AuthorizationCodeResponse authorizationCodeResponse;

    public CallbackServerTest() {
        this.callbackServer = new CallbackServer(CallbackServerConfig
                .newBuilder()
                .withCallbackListener(this::callbackReceived)
                .build());
    }

    private void callbackReceived(AuthorizationCodeResponse authorizationCodeResponse) {
        this.authorizationCodeResponse = authorizationCodeResponse;
    }

    @BeforeEach
    public void start()
            throws IOException {
        this.callbackServer.start();
    }

    @AfterEach
    public void stop() {
        this.callbackServer.stop();
        this.authorizationCodeResponse = null;
    }

    @RepeatedTest(2)
    public void callbackSuccess()
            throws ResourceException {
        // given
        String callbackEndpoint = String.format(
                "%s?state=my-state&code=my-code", this.callbackServer.getSuccessEndpoint());
        GetRequest getRequest = new GetRequest(RequestParameters
                .newBuilder()
                .withEndpoint(callbackEndpoint)
                .build());

        // when
        GetResult getResult = new RemoteResource().get(getRequest);

        // then
        assertThat(getResult, is(not(equalTo(null))));
        assertThat(getResult.getResultCode(), is(equalTo(200)));
        assertThat(this.authorizationCodeResponse, is(not(equalTo(null))));
        assertThat(this.authorizationCodeResponse.getState(), is(equalTo("my-state")));
        assertThat(this.authorizationCodeResponse.getCode(), is(equalTo("my-code")));
    }

    @Test
    public void redirect()
            throws ResourceException {
        // given
        String url = String.format("%s?state=my-state&code=my-code", this.callbackServer.getSuccessEndpoint());
        String encodedUrl = Base64.encodeBase64URLSafeString(url.getBytes(StandardCharsets.UTF_8));
        String redirectEndpoint = String.format("%s?to=%s", this.callbackServer.getRedirectEndpoint(), encodedUrl);
        GetRequest getRequest = new GetRequest(RequestParameters
                .newBuilder()
                .withEndpoint(redirectEndpoint)
                .build());

        // when
        GetResult getResult = new RemoteResource().get(getRequest);

        // then
        assertThat(getResult, is(not(equalTo(null))));
        assertThat(getResult.getResultCode(), is(equalTo(200)));
        assertThat(this.authorizationCodeResponse, is(not(equalTo(null))));
        assertThat(this.authorizationCodeResponse.getState(), is(equalTo("my-state")));
        assertThat(this.authorizationCodeResponse.getCode(), is(equalTo("my-code")));
    }
}
