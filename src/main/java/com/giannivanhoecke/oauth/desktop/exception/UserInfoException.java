package com.giannivanhoecke.oauth.desktop.exception;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class UserInfoException extends OAuth2Exception {

    public UserInfoException(String message) {
        super(message);
    }

    public UserInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
