package com.giannivanhoecke.oauth.desktop.io.local;

import com.giannivanhoecke.oauth.desktop.io.HttpHeader;
import com.giannivanhoecke.oauth.desktop.io.HttpStatusCode;
import com.giannivanhoecke.oauth.desktop.representation.internal.AuthorizationCodeResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class SuccessHttpHandler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuccessHttpHandler.class);

    private static final String SUCCESS_PAGE_LOCATION = "/success.html";
    private static final boolean WITH_AUTO_CLOSE      = true;
    private static final boolean WITHOUT_AUTO_CLOSE   = false;
    
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
        if (this.callbackServerConfig.isCloseBrowserTabOnSuccess()) {
            htmlStatusCode = HttpStatusCode.OK;
            htmlResponse = returnDefaultSuccessHtml(WITH_AUTO_CLOSE);
        } else if (successRedirectUri.isEmpty()) {
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
            Optional<Module> module = getCurrentModule();
            if (module.isPresent()) {
                return returnSuccessHtmlFromModule(module.get());
            } else {
                return returnSuccessHtmlFromClass();
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Cannot read '%s' from resources", SUCCESS_PAGE_LOCATION), e);
        }
        return returnDefaultSuccessHtml();
    }
    
    private Optional<Module> getCurrentModule() {
        Module module = SuccessHttpHandler.class.getModule();
        if (module == null || StringUtils.isBlank(module.getName())) {
            return Optional.empty();
        }
        return Optional.of(module);
    }

    private String returnSuccessHtmlFromModule(Module module)
            throws IOException {
        try (InputStream stream = module.getResourceAsStream(SUCCESS_PAGE_LOCATION)) {
            return new String(stream.readAllBytes());
        }
    }

    private String returnSuccessHtmlFromClass()
            throws IOException {
        try (InputStream stream = SuccessHttpHandler.class.getResourceAsStream(SUCCESS_PAGE_LOCATION)) {
            if (stream != null) {
                return new String(stream.readAllBytes());
            } else {
                throw new IOException(String.format("Resource '%s' not found!", SUCCESS_PAGE_LOCATION));
            }
        }
    }

    private String returnDefaultSuccessHtml() {
        return returnDefaultSuccessHtml(WITHOUT_AUTO_CLOSE);
    }

    private String returnDefaultSuccessHtml(boolean autoClose) {
        return "<html><body>"
                + "<h1>Login Successful</h1>"
                + "<p>You may close this browser window and go back to your application.</p>"
                + (autoClose ? "<script>open(location, '_self').close();</script>" : "")
                + "</body></html>";
    }
}
