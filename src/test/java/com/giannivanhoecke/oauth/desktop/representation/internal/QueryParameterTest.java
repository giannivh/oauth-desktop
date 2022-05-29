package com.giannivanhoecke.oauth.desktop.representation.internal;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class QueryParameterTest {

    @Test
    public void getParamMap() {
        // given
        String query = "key1=value1&key2=value2&key3=value3";

        // when
        Map<String, String> paramMap = QueryParameter.getParamMap(query);

        // then
        assertThat(paramMap, is(not(equalTo(null))));
        assertThat(paramMap.size(), is(equalTo(3)));
        assertThat(paramMap.get("key1"), is(equalTo("value1")));
        assertThat(paramMap.get("key2"), is(equalTo("value2")));
        assertThat(paramMap.get("key3"), is(equalTo("value3")));
    }

    @Test
    public void getParamMapNullQuery() {
        // given
        String query = null;

        // when
        Map<String, String> paramMap = QueryParameter.getParamMap(query);

        // then
        assertThat(paramMap, is(not(equalTo(null))));
        assertThat(paramMap.isEmpty(), is(true));
    }

    @Test
    public void getParamMapEmptyQuery() {
        // given
        String query = "";

        // when
        Map<String, String> paramMap = QueryParameter.getParamMap(query);

        // then
        assertThat(paramMap, is(not(equalTo(null))));
        assertThat(paramMap.isEmpty(), is(true));
    }

    @Test
    public void getParamMapNonQuery() {
        // given
        String query = "i-am-just-a-string";

        // when
        Map<String, String> paramMap = QueryParameter.getParamMap(query);

        // then
        assertThat(paramMap, is(not(equalTo(null))));
        assertThat(paramMap.isEmpty(), is(true));
    }
}
