package com.giannivanhoecke.oauth.desktop.fixtures;

import com.giannivanhoecke.oauth.desktop.representation.internal.QueryParameter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class AuthorizationServerMock {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServerMock.class);

    public static final String VALID_ACCESS_TOKEN = "valid-access-token";
    public static final String VALID_REFRESH_TOKEN = "valid-refresh-token";
    public static final String VALID_CLIENT_ID = "valid-client-id";
    public static final String VALID_AUTHORIZATION_CODE = "valid-authorization-code";

    private static final String HOSTNAME = "localhost";
    private static final int RANDOM_AVAILABLE_PORT = 0;
    private static final int WITHOUT_REQUEST_QUEUEING = 0;

    private final HttpServer server;
    private final ScheduledExecutorService scheduledExecutorService;

    private String authState = null;
    private String authCode = null;

    public AuthorizationServerMock()
            throws IOException {
        server = HttpServer
                .create(new InetSocketAddress(HOSTNAME, RANDOM_AVAILABLE_PORT), WITHOUT_REQUEST_QUEUEING);
        mockAuthEndpoint();
        mockUserInfoEndpoint();
        mockTokenEndpoint();
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.server.setExecutor(scheduledExecutorService);
    }

    public void start() {
        this.server.start();
        LOGGER.debug("Server started on host '{}' and port {}", HOSTNAME, this.server.getAddress().getPort());
    }

    public void stop() {
        this.server.stop(0);
        this.scheduledExecutorService.shutdown();
        LOGGER.debug("Server stopped");
    }

    public String getBaseUrl() {
        return String.format("http://%s:%d", HOSTNAME, this.server.getAddress().getPort());
    }

    public void overrideAuthState(String authState) {
        this.authState = authState;
    }

    public void overrideAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void resetAuth() {
        overrideAuthState(null);
        overrideAuthCode(null);
    }

    // mocked endpoints

    private void mockAuthEndpoint() {
        server.createContext("/auth", exchange -> {
            LOGGER.debug("Received '/auth' request, mocking successful login");
            Map<String, String> paramMap = QueryParameter.getParamMap(exchange.getRequestURI().getQuery());
            String state = this.authState != null ? this.authState : paramMap.get("state");
            String code = this.authCode != null ? this.authCode : VALID_AUTHORIZATION_CODE;
            String redirectUri = URLDecoder.decode(paramMap.get("redirect_uri"), StandardCharsets.UTF_8);
            String location = String.format("%s?state=%s&code=%s", redirectUri, state, code);
            exchange.getResponseHeaders().add("Location", location);
            sendResponse(exchange, 301, "Redirecting...");
        });
    }

    private void mockUserInfoEndpoint() {
        server.createContext("/userinfo", exchange -> {
            LOGGER.debug("Received '/userinfo' request");
            if (hasValidBearerToken(exchange)) {
                sendResponse(exchange, 200, "{\"sub\":\"9047370c-24f9-45f1-9959-faca5e60e6b9\",\"email_verified\":true,\"name\":\"Gianni Van Hoecke\",\"preferred_username\":\"gianni@giannivanhoecke.com\",\"given_name\":\"Gianni\",\"locale\":\"en\",\"family_name\":\"Van Hoecke\",\"email\":\"gianni@giannivanhoecke.com\"}");
            } else {
                sendResponse(exchange, 401, "Unauthorized");
            }
        });
    }

    private void mockTokenEndpoint() {
        server.createContext("/token", exchange -> {
            LOGGER.debug("Received '/token' request");
            String body = getRequestBody(exchange);
            Map<String, String> paramMap = QueryParameter.getParamMap(body);
            if (isValidAuthorizationCodeRequest(paramMap.get("grant_type"), paramMap.get("code"))) {
                sendResponse(exchange, 200, String.format("{\"access_token\":\"%s\",\"expires_in\":300,\"refresh_expires_in\":0,\"refresh_token\":\"%s\",\"not-before-policy\":0,\"session_state\":\"3dac9c9b-bcd8-4ed4-82c4-df618cbcb3ad\",\"scope\":\"openid offline_access email profile\"}", VALID_ACCESS_TOKEN, VALID_REFRESH_TOKEN));
            } else if (isValidRefreshTokenRequest(body)) {
                sendResponse(exchange, 200, "{\"access_token\":\"new-access-token\",\"expires_in\":300,\"refresh_expires_in\":0,\"refresh_token\":\"new-refresh-token\",\"not-before-policy\":0,\"session_state\":\"3dac9c9b-bcd8-4ed4-82c4-df618cbcb3ad\",\"scope\":\"openid offline_access email profile\"}");
            } else {
                sendResponse(exchange, 401, "Unauthorized");
            }
        });
    }

    // util

    private boolean hasValidBearerToken(HttpExchange exchange) {
        String authorization = exchange.getRequestHeaders().getFirst("Authorization");
        return StringUtils.isNotBlank(authorization) && getValidBearer().equals(authorization);
    }

    private String getValidBearer() {
        return String.format("Bearer %s", VALID_ACCESS_TOKEN);
    }

    private String getRequestBody(HttpExchange exchange)
            throws IOException {
        BufferedReader httpInput = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        StringBuilder in = new StringBuilder();
        String input;
        while ((input = httpInput.readLine()) != null) {
            in.append(input).append(" ");
        }
        httpInput.close();
        return in.toString().trim();
    }

    private boolean isValidAuthorizationCodeRequest(String grantType, String code) {
        return StringUtils.isNotBlank(grantType) && StringUtils.isNotBlank(code)
                && grantType.equals("authorization_code") && code.equals(VALID_AUTHORIZATION_CODE);
    }
    
    private boolean isValidRefreshTokenRequest(String form) {
        return StringUtils.isNotBlank(form) && form.contains(String.format("refresh_token=%s", VALID_REFRESH_TOKEN));
    }

    private void sendResponse(HttpExchange exchange, int httpCode, String htmlResponse)
            throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(httpCode, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
