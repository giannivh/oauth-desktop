package com.giannivanhoecke.oauth.desktop.exception;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class VerificationException extends OAuth2Exception {

    public VerificationException(String message) {
        super(message);
    }
}
