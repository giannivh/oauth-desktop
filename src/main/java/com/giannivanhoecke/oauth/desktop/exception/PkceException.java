package com.giannivanhoecke.oauth.desktop.exception;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class PkceException extends OAuth2Exception {

    public PkceException(String message, Throwable cause) {
        super(message, cause);
    }
}
