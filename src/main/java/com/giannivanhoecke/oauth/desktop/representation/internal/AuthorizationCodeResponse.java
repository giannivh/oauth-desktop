package com.giannivanhoecke.oauth.desktop.representation.internal;

import java.util.Map;
import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class AuthorizationCodeResponse {

    private final String state;
    private final String code;

    AuthorizationCodeResponse(String state, String code) {
        this.state = state;
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AuthorizationCodeResponse{" + "state='" + state + '\'' + ", code='" + code + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorizationCodeResponse)) {
            return false;
        }
        AuthorizationCodeResponse that = (AuthorizationCodeResponse) o;
        return Objects.equals(state, that.state) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, code);
    }

    public static AuthorizationCodeResponse parse(String query) {
        Map<String, String> paramMap = QueryParameter.getParamMap(query);
        return new AuthorizationCodeResponse(paramMap.get("state"), paramMap.get("code"));
    }
}
