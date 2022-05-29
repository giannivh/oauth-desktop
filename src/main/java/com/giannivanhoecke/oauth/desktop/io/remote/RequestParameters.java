package com.giannivanhoecke.oauth.desktop.io.remote;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class RequestParameters {

    private final int connectTimeoutInMs;
    private final int requestTimeoutInMs;
    private final String endpoint;
    private final KeyValuePair[] keyValuePairs;

    private RequestParameters(Builder builder) {
        connectTimeoutInMs = builder.connectTimeoutInMs;
        requestTimeoutInMs = builder.requestTimeoutInMs;
        endpoint = builder.endpoint;
        keyValuePairs = builder.keyValuePairs;
    }

    public int getConnectTimeoutInMs() {
        return connectTimeoutInMs;
    }

    public int getRequestTimeoutInMs() {
        return requestTimeoutInMs;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public KeyValuePair[] getKeyValuePairs() {
        return keyValuePairs;
    }

    @Override
    public String toString() {
        return "RequestParameters{" + "connectTimeoutInMs=" + connectTimeoutInMs + ", requestTimeoutInMs=" +
                requestTimeoutInMs + ", endpoint='" + endpoint + '\'' + ", keyValuePairs=" +
                Arrays.toString(keyValuePairs) + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestParameters)) {
            return false;
        }
        RequestParameters that = (RequestParameters) o;
        return connectTimeoutInMs == that.connectTimeoutInMs && requestTimeoutInMs == that.requestTimeoutInMs &&
                Objects.equals(endpoint, that.endpoint) && Arrays.equals(keyValuePairs, that.keyValuePairs);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(connectTimeoutInMs, requestTimeoutInMs, endpoint);
        result = 31 * result + Arrays.hashCode(keyValuePairs);
        return result;
    }

    // util

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        
        private static final int DEFAULT_TIMEOUT_CONNECT = 10_000;
        private static final int DEFAULT_TIMEOUT_REQUEST = 300_000;
        
        private int connectTimeoutInMs;
        private int requestTimeoutInMs;
        private String endpoint;
        private KeyValuePair[] keyValuePairs;

        private Builder() {
            connectTimeoutInMs = DEFAULT_TIMEOUT_CONNECT;
            requestTimeoutInMs = DEFAULT_TIMEOUT_REQUEST;
        }

        public Builder withConnectTimeoutInMs(int val) {
            connectTimeoutInMs = val;
            return this;
        }

        public Builder withRequestTimeoutInMs(int val) {
            requestTimeoutInMs = val;
            return this;
        }

        public Builder withEndpoint(String val) {
            endpoint = val;
            return this;
        }

        public Builder withKeyValuePairs(KeyValuePair... val) {
            keyValuePairs = val;
            return this;
        }

        public RequestParameters build() {
            return new RequestParameters(this);
        }
    }
}
