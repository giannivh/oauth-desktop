package com.giannivanhoecke.oauth.desktop.representation;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class AccessTokenResponseTest {

    private static final String ACCESS_TOKEN  = "my-access-token";
    private static final String REFRESH_TOKEN = "my-refresh-token";
    private static final String ID_TOKEN      = "my-id-token";
    private static final String TOKEN_TYPE    = "my-token-type";
    private static final int    EXPIRES_IN    = 1;

    @Test
    public void constructor() {
        // given
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);

        // then
        assertThat(accessTokenResponse.getAccessToken(), is(equalTo(ACCESS_TOKEN)));
        assertThat(accessTokenResponse.getRefreshToken(), is(equalTo(REFRESH_TOKEN)));
        assertThat(accessTokenResponse.getIdToken(), is(equalTo(ID_TOKEN)));
        assertThat(accessTokenResponse.getTokenType(), is(equalTo(TOKEN_TYPE)));
        assertThat(accessTokenResponse.getExpiresIn(), is(equalTo(EXPIRES_IN)));
    }

    @Test
    public void equalsTrue() {
        // given
        AccessTokenResponse thisAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);
        AccessTokenResponse otherAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);

        // when
        boolean equalsThis = thisAccessTokenResponse.equals(otherAccessTokenResponse);
        boolean equalsOther = otherAccessTokenResponse.equals(thisAccessTokenResponse);

        // then
        assertThat(equalsThis, is(true));
        assertThat(equalsOther, is(true));
    }

    @Test
    public void equalsFalseAccessToken() {
        // given
        AccessTokenResponse thisAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);
        AccessTokenResponse otherAccessTokenResponse = new AccessTokenResponse(
                "other-access-token", REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);

        // when
        boolean equalsThis = thisAccessTokenResponse.equals(otherAccessTokenResponse);
        boolean equalsOther = otherAccessTokenResponse.equals(thisAccessTokenResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseRefreshToken() {
        // given
        AccessTokenResponse thisAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);
        AccessTokenResponse otherAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, "other-refresh-token", ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);

        // when
        boolean equalsThis = thisAccessTokenResponse.equals(otherAccessTokenResponse);
        boolean equalsOther = otherAccessTokenResponse.equals(thisAccessTokenResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseIdToken() {
        // given
        AccessTokenResponse thisAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);
        AccessTokenResponse otherAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, "other-id-token", TOKEN_TYPE, EXPIRES_IN);

        // when
        boolean equalsThis = thisAccessTokenResponse.equals(otherAccessTokenResponse);
        boolean equalsOther = otherAccessTokenResponse.equals(thisAccessTokenResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseTokenType() {
        // given
        AccessTokenResponse thisAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);
        AccessTokenResponse otherAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, "other-token-type", EXPIRES_IN);

        // when
        boolean equalsThis = thisAccessTokenResponse.equals(otherAccessTokenResponse);
        boolean equalsOther = otherAccessTokenResponse.equals(thisAccessTokenResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseExpiresIn() {
        // given
        AccessTokenResponse thisAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);
        AccessTokenResponse otherAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, 2);

        // when
        boolean equalsThis = thisAccessTokenResponse.equals(otherAccessTokenResponse);
        boolean equalsOther = otherAccessTokenResponse.equals(thisAccessTokenResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsHashCode() {
        // given
        AccessTokenResponse thisAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);
        AccessTokenResponse otherAccessTokenResponse = new AccessTokenResponse(
                ACCESS_TOKEN, REFRESH_TOKEN, ID_TOKEN, TOKEN_TYPE, EXPIRES_IN);

        // when
        int thisHashCode = thisAccessTokenResponse.hashCode();
        int otherHashCode = otherAccessTokenResponse.hashCode();

        // then
        assertThat(thisHashCode, is(equalTo(otherHashCode)));
    }
}
