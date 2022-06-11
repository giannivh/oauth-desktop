package com.giannivanhoecke.oauth.desktop.io.local;

import com.giannivanhoecke.oauth.desktop.io.HttpHeader;
import com.giannivanhoecke.oauth.desktop.io.HttpStatusCode;
import com.giannivanhoecke.oauth.desktop.representation.internal.QueryParameter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author gvhoecke {@literal <gianni.vanhoecke@unifiedpost.com>}
 * @since 1.1
 */
public class RedirectHttpHandler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectHttpHandler.class);

    private static final String PARAM_REDIRECT_TO = "to";

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {
        String redirectTo = getRedirectEndpoint(exchange);
        handleRedirect(exchange, redirectTo);
    }

    // util

    private String getRedirectEndpoint(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        LOGGER.debug("Received redirect request with query '{}'", query);
        Map<String, String> paramMap = QueryParameter.getParamMap(query);
        return new String(Base64.decodeBase64(paramMap.get(PARAM_REDIRECT_TO)));
    }

    private void handleRedirect(HttpExchange exchange, String redirectTo)
            throws IOException {
        LOGGER.debug("Redirecting to '{}'", redirectTo);
        OutputStream outputStream = exchange.getResponseBody();
        int htmlStatusCode = HttpStatusCode.MOVED_PERMANENTLY;
        String htmlResponse = "Redirecting...";
        exchange.getResponseHeaders().add(HttpHeader.LOCATION, redirectTo);
        exchange.sendResponseHeaders(htmlStatusCode, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
