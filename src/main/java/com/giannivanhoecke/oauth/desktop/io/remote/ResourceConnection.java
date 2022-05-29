package com.giannivanhoecke.oauth.desktop.io.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class ResourceConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceConnection.class);

    private final RequestMethod requestMethod;
    private final boolean output;
    private final String url;
    private final KeyValuePair[] keyValuePairs;
    private final String payload;
    private final int connectTimeoutInMs;
    private final int requestTimeoutInMs;

    private ResourceConnection(Builder builder) {
        this.requestMethod = builder.requestMethod;
        this.output = builder.output;
        this.url = builder.url;
        this.keyValuePairs = builder.keyValuePairs == null ? new KeyValuePair[0] : builder.keyValuePairs;
        this.payload = builder.payload;
        this.connectTimeoutInMs = builder.connectTimeoutInMs;
        this.requestTimeoutInMs = builder.requestTimeoutInMs;
    }

    public HttpResponse<String> exec()
            throws IOException {
        return sendHttpRequest(HttpResponse.BodyHandlers.ofString());
    }
    
    private <T> HttpResponse<T> sendHttpRequest(HttpResponse.BodyHandler<T> bodyHandler)
            throws IOException {
        try {
            HttpClient httpClient = HttpClient
                    .newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofMillis(this.connectTimeoutInMs))
                    .proxy(ProxySelector.getDefault())
                    .build();
            HttpResponse<T> httpResponse = httpClient.send(toHttpRequest(), bodyHandler);
            MDC.put(RemoteResourceMdcConstants.LOGGER_MDC_HTTP_RESPONSE_CODE, Integer.toString(httpResponse.statusCode()));
            LOGGER.debug("Request sent to resource");
            return httpResponse;
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
    
    private HttpRequest toHttpRequest()
            throws IOException {
        try {
            HttpRequest.Builder httpRequestBuilder = HttpRequest
                    .newBuilder()
                    .uri(new URI(this.url))
                    .method(this.requestMethod.name(), getBodyPublisher())
                    .timeout(Duration.of(this.requestTimeoutInMs, ChronoUnit.MILLIS));
            for (KeyValuePair keyValuePair : this.keyValuePairs) {
                httpRequestBuilder.header(keyValuePair.getKey(), keyValuePair.getValue());
            }
            return httpRequestBuilder.build();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private HttpRequest.BodyPublisher getBodyPublisher() {
        if (output) {
            return HttpRequest.BodyPublishers.ofString(this.payload);
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    // static util
    
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private RequestMethod requestMethod = RequestMethod.GET;
        private boolean output = false;
        private String url;
        private KeyValuePair[] keyValuePairs;
        private String payload;
        private int connectTimeoutInMs;
        private int requestTimeoutInMs;

        private Builder() {
        }

        public Builder withRequestMethod(RequestMethod val) {
            requestMethod = val;
            return this;
        }

        public Builder withOutput(boolean val) {
            output = val;
            return this;
        }

        public Builder withUrl(String val) {
            url = val;
            return this;
        }

        public Builder withKeyValuePairs(KeyValuePair[] val) {
            keyValuePairs = val;
            return this;
        }

        public Builder withPayload(String val) {
            payload = val;
            return this;
        }

        public Builder withConnectTimeoutInMs(int val) {
            connectTimeoutInMs = val;
            return this;
        }

        public Builder withRequestTimeoutInMs(int val) {
            requestTimeoutInMs = val;
            return this;
        }

        public ResourceConnection build() {
            return new ResourceConnection(this);
        }
    }
}
