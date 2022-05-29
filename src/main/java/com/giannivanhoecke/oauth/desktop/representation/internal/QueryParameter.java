package com.giannivanhoecke.oauth.desktop.representation.internal;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class QueryParameter {

    public static Map<String, String> getParamMap(String query) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyMap();
        }
        return Stream.of(query.split("&"))
                .filter(s -> !s.isEmpty())
                .map(kv -> kv.split("=", 2))
                .filter(s -> s.length == 2)
                .collect(Collectors.toMap(x -> x[0].toLowerCase(), x-> x[1]));
    }
}
