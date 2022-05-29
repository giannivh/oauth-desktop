package com.giannivanhoecke.oauth.desktop.exception;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class CallbackServerException extends OAuth2Exception {
    
    public CallbackServerException(String message) {
        super(message);
    }
    
    public CallbackServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
