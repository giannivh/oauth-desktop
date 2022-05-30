# OAuth2 Desktop Library

Open source OAuth2 desktop library for Java.

This library allows you to:
* Start an authorization code flow with PKCE, using the system's browser
* Get user info by access token
* Exchange a refresh token into new tokens

## Motivation

We had to write a native modular desktop application in Java where the user had to log in using Keycloak.

We were using the Keycloak Installed adapter, and that worked up until Keycloak version 16. The adapter redirected back 
to `/delegated` but that endpoint got removed in version 16. They didn't update the adapter. Our users would see a `404` 
after successfully logging in. That caused confusion.

Also, we had to hack modularity in the Keycloak Installed adapter, as it is not modular. Next to that, the adapter 
pulled in a lot of heavy dependencies like Undertow, JBoss RESTEasy, and whatnot. It also relied on Java EE, which has 
been deprecated since Java 9. Generating a jlink image was a pain.

We had to replace the Keycloak Installed adapter with something else, but what was available to use was either 
discontinued, not modular, or bloatware pulling in a lot of dependencies.

For our little needs, we decided it's better to write our own library with the following requirements:
* It has to be lightweight
* It needs to support Java modularity
* It should rely on as little as possible third-party dependencies
* It needs to be easy to use
              
## Support

This has only been tested using Keycloak as an authorization server.

## Quickstart
            
### Dependency

If you are using Maven, add this to your pom.xml file:

```xml
<dependency>
  <groupId>com.giannivanhoecke.oauth</groupId>
  <artifactId>oauth-desktop</artifactId>
  <version>1.0</version>
</dependency>
```

If you are using Gradle, add this to your dependencies:

```groovy
implementation 'com.giannivanhoecke.oauth:oauth-desktop:1.0'
```

### Java module

In your `module-info.java`, add:

```java
requires com.giannivanhoecke.oauth.desktop;
```

## Usage
         
### Configuration

Start by configuring your authorization server using the default configuration:

```java
AuthorizationServerConfig config = AuthorizationServerConfig
        .newBuilder()
        .withBaseUrl("http://localhost:8082/auth/realms/test/protocol/openid-connect")
        .withClientId("myclient")
        .build();
```

This will show a built-in HTML success page after authorization.

Alternatively, you can override the `auth`, `userinfo`, and `token` endpoints, as well as the `scope`:

```java
AuthorizationServerConfig config = AuthorizationServerConfig
        .newBuilder()
        .withBaseUrl("http://localhost:8082/auth/realms/test/protocol/openid-connect")
        .withEndpointAuth("/auth")
        .withEndpointUserInfo("/userinfo")
        .withEndpointToken("/token")
        .withClientId("myclient")
        .withAuthScope("openid offline_access email profile")
        // either redirect to your own URI ...
        .withSuccessRedirectUri("http://localhost/success.html")
        // ... or automatically close the browser tab
        .withCloseBrowserTabOnSuccess(true)
        .build();
```

The optional `.withSuccessRedirectUri("http://localhost/success.html")` will redirect to your page of choice instead of 
showing the built-in HTML success page.

The optional `.withCloseBrowserTabOnSuccess(true)` will automatically close the browser tab instead of showing the
built-in HTML success page. Please note: this property takes precedence over your own success redirect URI.
     
### Instantiate

Next, instantiate using the system's default browser:

```java
authorizationCodeFlowWithPkce = new AuthorizationCodeFlowWithPkce(config);
```

Alternatively, you can supply your own `Browser` implementation, if the native Java `Desktop` isn't suitable for your 
use case:

```java
authorizationCodeFlowWithPkce = new AuthorizationCodeFlowWithPkce(config, this::open);
```

### Authorize

To start the authorization flow using PKCE:

```java
Future<AccessTokenResponse> accessTokenResponseFuture = authorizationCodeFlowWithPkce.authorize();
AccessTokenResponse accessTokenResponse = accessTokenResponseFuture.get(5, TimeUnit.MINUTES);

LOGGER.info("Success! I got:");
LOGGER.info("  -> access token:  " + accessTokenResponse.getAccessToken());
LOGGER.info("  -> refresh token: " + accessTokenResponse.getRefreshToken());
```

You will get a `Future<AccessTokenResponse>` instance, as we need to wait on an external action 
(the user authorizing in the browser).

### Exchange refresh token
           
In order to exchange a refresh token for new tokens:

```java
AccessTokenResponse accessTokenResponse = authorizationCodeFlowWithPkce.refresh(refreshToken);

LOGGER.info("Success! I got:");
LOGGER.info("  -> access token:  " + accessTokenResponse.getAccessToken());
LOGGER.info("  -> refresh token: " + accessTokenResponse.getRefreshToken());
```

### Get user info

If you want retrieve some basic user info, or if you just want to test if your access token is still valid:

```java
UserInfoResponse userInfoResponse = authorizationCodeFlowWithPkce.getUserInfo(accessToken);

LOGGER.info("Success! I got:");
LOGGER.info("  -> ID:       " + userInfoResponse.getId());
LOGGER.info("  -> Username: " + userInfoResponse.getUsername());
LOGGER.info("  -> Name:     " + userInfoResponse.getName());
LOGGER.info("  -> Email:    " + userInfoResponse.getEmail());
```

### Exceptions   

The OAuth2 Desktop Library throws unchecked exceptions. Check the JavaDocs for each method.
If you want to catch all at once, it suffices to catch `OAuth2Exception` for any method.

## License

MIT license - See [LICENSE](LICENSE) for more information.