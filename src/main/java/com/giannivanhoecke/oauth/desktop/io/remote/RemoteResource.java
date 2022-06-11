package com.giannivanhoecke.oauth.desktop.io.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.http.HttpResponse;
import java.util.Arrays;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class RemoteResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteResource.class);

    public GetResult get(GetRequest getRequest)
            throws ResourceException {
        RequestParameters requestParameters = getRequest.getRequestParameters();
        try {
            MDC.put(RemoteResourceMdcConstants.LOGGER_MDC_REMOTE_LOCATION, requestParameters.getEndpoint());
            MDC.put(RemoteResourceMdcConstants.LOGGER_MDC_KEY_VALUES, Arrays.toString(requestParameters.getKeyValuePairs()));
            LOGGER.debug("[{}] resource...", RequestMethod.GET.name());
            HttpResponse<String> response = ResourceConnection
                    .newBuilder()
                    .withRequestMethod(RequestMethod.GET)
                    .withUrl(requestParameters.getEndpoint())
                    .withKeyValuePairs(requestParameters.getKeyValuePairs())
                    .withConnectTimeoutInMs(requestParameters.getConnectTimeoutInMs())
                    .withRequestTimeoutInMs(requestParameters.getRequestTimeoutInMs())
                    .build()
                    .exec();
            return new GetResult(response.statusCode(), response.body());
        } catch (Exception e) {
            throw new ResourceException(requestParameters.getEndpoint(),
                    String.format("Can't get data: %s", e.getMessage()), e);
        } finally {
            clearMdc();
        }
    }

    public PostResult post(PostRequest postRequest)
            throws ResourceException {
        HttpResponse<String> response = sendPayload(
                postRequest.getRequestParameters(),
                postRequest.getPayload(),
                RequestMethod.POST);
        return new PostResult(response.statusCode(), response.body());
    }

    // util

    private HttpResponse<String> sendPayload(RequestParameters requestParameters, String payload,
                                             RequestMethod requestMethod)
            throws ResourceException {
        try {
            MDC.put(RemoteResourceMdcConstants.LOGGER_MDC_REMOTE_LOCATION, requestParameters.getEndpoint());
            MDC.put(RemoteResourceMdcConstants.LOGGER_MDC_PAYLOAD, payload);
            MDC.put(RemoteResourceMdcConstants.LOGGER_MDC_KEY_VALUES, Arrays.toString(requestParameters.getKeyValuePairs()));
            LOGGER.debug("[{}] resource...", requestMethod.name());
            return ResourceConnection
                    .newBuilder()
                    .withRequestMethod(requestMethod)
                    .withOutput(true)
                    .withUrl(requestParameters.getEndpoint())
                    .withKeyValuePairs(requestParameters.getKeyValuePairs())
                    .withPayload(payload)
                    .withConnectTimeoutInMs(requestParameters.getConnectTimeoutInMs())
                    .withRequestTimeoutInMs(requestParameters.getRequestTimeoutInMs())
                    .build()
                    .exec();
        } catch (Exception e) {
            throw new ResourceException(requestParameters.getEndpoint(),
                    String.format("Can't send data: %s", e.getMessage()), e);
        } finally {
            clearMdc();
        }
    }

    private void clearMdc() {
        MDC.remove(RemoteResourceMdcConstants.LOGGER_MDC_KEY_VALUES);
        MDC.remove(RemoteResourceMdcConstants.LOGGER_MDC_PAYLOAD);
        MDC.remove(RemoteResourceMdcConstants.LOGGER_MDC_REMOTE_LOCATION);
        MDC.remove(RemoteResourceMdcConstants.LOGGER_MDC_HTTP_RESPONSE_CODE);
    }
}
