package com.giannivanhoecke.oauth.desktop.representation;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class UserInfoResponseTest {

    private static final String ID       = "my-id";
    private static final String USERNAME = "my-username";
    private static final String NAME     = "my-name";
    private static final String EMAIL    = "my-email";

    @Test
    public void constructor() {
        // given
        UserInfoResponse userInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);

        // then
        assertThat(userInfoResponse.getId(), is(equalTo(ID)));
        assertThat(userInfoResponse.getUsername(), is(equalTo(USERNAME)));
        assertThat(userInfoResponse.getName(), is(equalTo(NAME)));
        assertThat(userInfoResponse.getEmail(), is(equalTo(EMAIL)));
    }

    @Test
    public void equalsTrue() {
        // given
        UserInfoResponse thisUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);
        UserInfoResponse otherUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);

        // when
        boolean equalsThis = thisUserInfoResponse.equals(otherUserInfoResponse);
        boolean equalsOther = otherUserInfoResponse.equals(thisUserInfoResponse);

        // then
        assertThat(equalsThis, is(true));
        assertThat(equalsOther, is(true));
    }

    @Test
    public void equalsFalseId() {
        // given
        UserInfoResponse thisUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);
        UserInfoResponse otherUserInfoResponse = new UserInfoResponse("other-id", USERNAME, NAME, EMAIL);

        // when
        boolean equalsThis = thisUserInfoResponse.equals(otherUserInfoResponse);
        boolean equalsOther = otherUserInfoResponse.equals(thisUserInfoResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseUsername() {
        // given
        UserInfoResponse thisUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);
        UserInfoResponse otherUserInfoResponse = new UserInfoResponse(ID, "other-username", NAME, EMAIL);

        // when
        boolean equalsThis = thisUserInfoResponse.equals(otherUserInfoResponse);
        boolean equalsOther = otherUserInfoResponse.equals(thisUserInfoResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseName() {
        // given
        UserInfoResponse thisUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);
        UserInfoResponse otherUserInfoResponse = new UserInfoResponse(ID, USERNAME, "other-name", EMAIL);

        // when
        boolean equalsThis = thisUserInfoResponse.equals(otherUserInfoResponse);
        boolean equalsOther = otherUserInfoResponse.equals(thisUserInfoResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsFalseEmail() {
        // given
        UserInfoResponse thisUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);
        UserInfoResponse otherUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, "other-email");

        // when
        boolean equalsThis = thisUserInfoResponse.equals(otherUserInfoResponse);
        boolean equalsOther = otherUserInfoResponse.equals(thisUserInfoResponse);

        // then
        assertThat(equalsThis, is(false));
        assertThat(equalsOther, is(false));
    }

    @Test
    public void equalsHashCode() {
        // given
        UserInfoResponse thisUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);
        UserInfoResponse otherUserInfoResponse = new UserInfoResponse(ID, USERNAME, NAME, EMAIL);

        // when
        int thisHashCode = thisUserInfoResponse.hashCode();
        int otherHashCode = otherUserInfoResponse.hashCode();

        // then
        assertThat(thisHashCode, is(equalTo(otherHashCode)));
    }
}
