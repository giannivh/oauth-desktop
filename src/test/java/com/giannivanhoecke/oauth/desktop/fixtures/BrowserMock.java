package com.giannivanhoecke.oauth.desktop.fixtures;

import com.giannivanhoecke.oauth.desktop.io.remote.GetRequest;
import com.giannivanhoecke.oauth.desktop.io.remote.RemoteResource;
import com.giannivanhoecke.oauth.desktop.io.remote.RequestParameters;
import com.giannivanhoecke.oauth.desktop.system.Browser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class BrowserMock implements Browser {
    
    @Override
    public void open(String uri) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> proceed(uri));
        executorService.shutdown();
    }

    // util

    private void proceed(String uri) {
        GetRequest getRequest = new GetRequest(RequestParameters
                .newBuilder()
                .withEndpoint(uri)
                .build());
        try {
            new RemoteResource().get(getRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
