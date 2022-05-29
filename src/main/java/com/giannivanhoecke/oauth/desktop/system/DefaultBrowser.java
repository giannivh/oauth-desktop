package com.giannivanhoecke.oauth.desktop.system;

import com.giannivanhoecke.oauth.desktop.exception.ActionNotSupportedException;
import com.giannivanhoecke.oauth.desktop.exception.BrowserException;

import java.awt.*;
import java.net.URI;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class DefaultBrowser implements Browser {

    @Override
    public void open(String uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(uri));
            } catch (Exception e) {
                throw new BrowserException("Cannot open browser", e);
            }
        } else {
            throw new ActionNotSupportedException("Cannot open browser: not supported");
        }
    }
}
