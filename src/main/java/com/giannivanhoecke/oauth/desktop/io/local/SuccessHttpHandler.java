package com.giannivanhoecke.oauth.desktop.io.local;

import com.giannivanhoecke.oauth.desktop.io.HttpHeader;
import com.giannivanhoecke.oauth.desktop.io.HttpStatusCode;
import com.giannivanhoecke.oauth.desktop.representation.internal.AuthorizationCodeResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class SuccessHttpHandler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuccessHttpHandler.class);

    private final CallbackServerConfig callbackServerConfig;

    public SuccessHttpHandler(CallbackServerConfig callbackServerConfig) {
        this.callbackServerConfig = callbackServerConfig;
    }

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {
        String query = exchange.getRequestURI().getQuery();
        LOGGER.debug("Received callback with query '{}'", query);
        AuthorizationCodeResponse codeResponse = AuthorizationCodeResponse.parse(query);
        handleResponse(exchange, codeResponse);
    }

    // util

    private void handleResponse(HttpExchange exchange, AuthorizationCodeResponse codeResponse)
            throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        Optional<String> successRedirectUri = this.callbackServerConfig.getSuccessRedirectUri();
        int htmlStatusCode;
        String htmlResponse;
        if (successRedirectUri.isEmpty()) {
            htmlStatusCode = HttpStatusCode.OK;
            htmlResponse = getSuccessHtml();
        } else {
            htmlStatusCode = HttpStatusCode.MOVED_PERMANENTLY;
            htmlResponse = "Redirecting...";
            exchange.getResponseHeaders().add(HttpHeader.LOCATION, successRedirectUri.get());
        }

        exchange.sendResponseHeaders(htmlStatusCode, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
        
        this.callbackServerConfig.getCallbackListener().onComplete(codeResponse);
    }

    private String getSuccessHtml() {
        try {
            return Files.readString(Paths.get(SuccessHttpHandler.class.getResource("/success.html").toURI()));
        } catch (Exception e) {
            LOGGER.error("Cannot read success.html from resources", e);
            return "<html><body>"
                    + "<h1>Login Successful</h1>"
                    + "<p>You may close this browser window and go back to your application.</p>"
                    + "</body></html>";
        }
    }
}
