package com.giannivanhoecke.oauth.desktop.extension;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class PkceTest {

    @Test
    public void generate() {
        // when
        Pkce pkce = Pkce.generate();

        // then
        assertThat(pkce, is(not(equalTo(null))));
        assertThat(pkce.getCodeVerifier(), is(not(equalTo(null))));
        assertThat(pkce.getCodeChallenge(), is(not(equalTo(null))));
        assertThat(pkce.getCodeVerifier(), is(not(emptyString())));
        assertThat(pkce.getCodeChallenge(), is(not(emptyString())));
    }
}
