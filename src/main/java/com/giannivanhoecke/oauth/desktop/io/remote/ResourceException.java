package com.giannivanhoecke.oauth.desktop.io.remote;

import java.io.IOException;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class ResourceException extends IOException {

    private static final long serialVersionUID = 1459916766836810587L;
    
    private final String url;

    public ResourceException(String url, String message, Throwable cause) {
        super(message, cause);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "FetchException{" + "url='" + url + '\'' + "} " + super.toString();
    }
}
