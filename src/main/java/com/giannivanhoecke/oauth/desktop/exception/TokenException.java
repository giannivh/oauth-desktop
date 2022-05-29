package com.giannivanhoecke.oauth.desktop.exception;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class TokenException extends OAuth2Exception {

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
