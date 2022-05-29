/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
module com.giannivanhoecke.oauth.desktop {

    requires jdk.httpserver;
    requires java.desktop;
    requires java.net.http;

    requires com.google.gson;
    requires org.apache.commons.lang3;
    requires org.apache.commons.codec;
    requires org.slf4j;

    opens com.giannivanhoecke.oauth.desktop.representation to com.google.gson;

    exports com.giannivanhoecke.oauth.desktop;
    exports com.giannivanhoecke.oauth.desktop.exception;
    exports com.giannivanhoecke.oauth.desktop.representation;
    exports com.giannivanhoecke.oauth.desktop.system;
}