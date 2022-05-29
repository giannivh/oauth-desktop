package com.giannivanhoecke.oauth.desktop.io.local;

import com.giannivanhoecke.oauth.desktop.exception.CallbackServerException;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class CallbackServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackServer.class);

    private static final String HOSTNAME = "localhost";
    private static final int RANDOM_AVAILABLE_PORT = 0;
    private static final int WITHOUT_REQUEST_QUEUEING = 0;
    private static final String ENDPOINT_SUCCESS = "/success";
    private static final int NO_DELAY = 0;

    private final CallbackServerConfig callbackServerConfig;

    private HttpServer server;
    private ScheduledExecutorService scheduledExecutorService;

    public CallbackServer(CallbackServerConfig callbackServerConfig) {
        this.callbackServerConfig = callbackServerConfig;
    }

    public void start()
            throws IOException {
        if (this.server != null) {
            stop();
        }
        this.server = HttpServer.create(
                new InetSocketAddress(HOSTNAME, RANDOM_AVAILABLE_PORT),
                WITHOUT_REQUEST_QUEUEING);
        this.server.createContext(ENDPOINT_SUCCESS, new SuccessHttpHandler(this.callbackServerConfig));
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.server.setExecutor(scheduledExecutorService);
        this.server.start();
        LOGGER.debug("Server started on host '{}' and port {}", HOSTNAME, this.server.getAddress().getPort());
    }

    public void stop() {
        if (this.server != null) {
            this.server.stop(NO_DELAY);
            this.scheduledExecutorService.shutdown();
            LOGGER.debug("Server stopped");
        }
    }

    public String getSuccessEndpoint() {
        if (this.server == null) {
            throw new CallbackServerException("Server isn't running, cannot request endpoint");
        }
        return String.format("http://%s:%d%s", HOSTNAME, this.server.getAddress().getPort(), ENDPOINT_SUCCESS);
    }
}
