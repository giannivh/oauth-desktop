package com.giannivanhoecke.oauth.desktop.io.remote;

import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class PostRequest {
    
    private final RequestParameters requestParameters;
    private final String payload;

    public PostRequest(RequestParameters requestParameters, String payload) {
        this.requestParameters = requestParameters;
        this.payload = payload;
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "PostRequest{" + "requestParameters=" + requestParameters + ", payload='" + payload + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostRequest)) {
            return false;
        }
        PostRequest that = (PostRequest) o;
        return Objects.equals(requestParameters, that.requestParameters) && Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestParameters, payload);
    }
}
