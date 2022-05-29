package com.giannivanhoecke.oauth.desktop.exception;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class ActionNotSupportedException extends OAuth2Exception {

    public ActionNotSupportedException(String message) {
        super(message);
    }
}
