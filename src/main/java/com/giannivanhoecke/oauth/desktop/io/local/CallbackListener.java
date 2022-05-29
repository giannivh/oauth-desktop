package com.giannivanhoecke.oauth.desktop.io.local;

import com.giannivanhoecke.oauth.desktop.representation.internal.AuthorizationCodeResponse;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public interface CallbackListener {

    void onComplete(AuthorizationCodeResponse authorizationCodeResponse);
}
