package com.giannivanhoecke.oauth.desktop.representation.internal;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class AuthorizationCodeResponseTest {

    private static final String STATE = "my-state";
    private static final String CODE  = "my-code";

    @Test
    public void constructor() {
        // given
        AuthorizationCodeResponse authCode = new AuthorizationCodeResponse(STATE, CODE);

        // then
        assertThat(authCode.getState(), is(equalTo(STATE)));
        assertThat(authCode.getCode(), is(equalTo(CODE)));
    }

    @Test
    public void equalsTrue() {
        // given
        AuthorizationCodeResponse thisAuthCode = new AuthorizationCodeResponse(STATE, CODE);
        AuthorizationCodeResponse otherAuthCode = new AuthorizationCodeResponse(STATE, CODE);

        // when
        boolean equalsThis = thisAuthCode.equals(otherAuthCode);
        boolean equalsOther = otherAuthCode.equals(thisAuthCode);

        // then
        assertThat(equalsThis, is(true));
        assertThat(equalsOther, is(true));
    }

    @Test
    public void equalsFalseState() {
        // given
        AuthorizationCodeResponse thisAuthCode = new AuthorizationCodeResponse(STATE, CODE);
        AuthorizationCodeResponse otherAuthCode = new AuthorizationCodeResponse("other-state", CODE);

        // when
        boolean equalsThis = thisAuthCode.equals(otherAuthCode);
        boolean equalsOther = otherAuthCode.equals(thisAuthCode);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseCode() {
        // given
        AuthorizationCodeResponse thisAuthCode = new AuthorizationCodeResponse(STATE, CODE);
        AuthorizationCodeResponse otherAuthCode = new AuthorizationCodeResponse(STATE, "other-code");

        // when
        boolean equalsThis = thisAuthCode.equals(otherAuthCode);
        boolean equalsOther = otherAuthCode.equals(thisAuthCode);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsHashCode() {
        // given
        AuthorizationCodeResponse thisAuthCode = new AuthorizationCodeResponse(STATE, CODE);
        AuthorizationCodeResponse otherAuthCode = new AuthorizationCodeResponse(STATE, CODE);

        // when
        int thisHashCode = thisAuthCode.hashCode();
        int otherHashCode = otherAuthCode.hashCode();

        // then
        assertThat(thisHashCode, is(equalTo(otherHashCode)));
    }

    @Test
    public void parse() {
        // given
        String query = String.format(
                "state=%s&session_state=9577ac28-b005-433c-87a5-df576d321bd8&code=%s",
                STATE, CODE);

        // when
        AuthorizationCodeResponse authCode = AuthorizationCodeResponse.parse(query);

        // then
        assertThat(authCode, is(not(equalTo(null))));
        assertThat(authCode.getState(), is(equalTo(STATE)));
        assertThat(authCode.getCode(), is(equalTo(CODE)));
    }

    @Test
    public void parseNullQuery() {
        // given
        String query = null;

        // when
        AuthorizationCodeResponse authCode = AuthorizationCodeResponse.parse(query);

        // then
        assertThat(authCode, is(not(equalTo(null))));
        assertThat(authCode.getState(), is(equalTo(null)));
        assertThat(authCode.getCode(), is(equalTo(null)));
    }

    @Test
    public void parseEmptyQuery() {
        // given
        String query = "";

        // when
        AuthorizationCodeResponse authCode = AuthorizationCodeResponse.parse(query);

        // then
        assertThat(authCode, is(not(equalTo(null))));
        assertThat(authCode.getState(), is(equalTo(null)));
        assertThat(authCode.getCode(), is(equalTo(null)));
    }

    @Test
    public void parseNoState() {
        // given
        String query = String.format("session_state=9577ac28-b005-433c-87a5-df576d321bd8&code=%s", CODE);

        // when
        AuthorizationCodeResponse authCode = AuthorizationCodeResponse.parse(query);

        // then
        assertThat(authCode, is(not(equalTo(null))));
        assertThat(authCode.getState(), is(equalTo(null)));
        assertThat(authCode.getCode(), is(equalTo(CODE)));
    }

    @Test
    public void parseNoCode() {
        // given
        String query = String.format("state=%s&session_state=9577ac28-b005-433c-87a5-df576d321bd8", STATE);

        // when
        AuthorizationCodeResponse authCode = AuthorizationCodeResponse.parse(query);

        // then
        assertThat(authCode, is(not(equalTo(null))));
        assertThat(authCode.getState(), is(equalTo(STATE)));
        assertThat(authCode.getCode(), is(equalTo(null)));
    }
}
