package com.giannivanhoecke.oauth.desktop.io.remote;

import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class GetRequest {

    private final RequestParameters requestParameters;

    public GetRequest(RequestParameters requestParameters) {
        this.requestParameters = requestParameters;
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    @Override
    public String toString() {
        return "GetRequest{" + "requestParameters=" + requestParameters + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GetRequest)) {
            return false;
        }
        GetRequest that = (GetRequest) o;
        return Objects.equals(requestParameters, that.requestParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestParameters);
    }
}
