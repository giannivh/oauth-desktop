package com.giannivanhoecke.oauth.desktop.representation;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class AccessTokenResponse {

    @SerializedName(value = "access_token")
    private String accessToken;
    @SerializedName(value = "refresh_token")
    private String refreshToken;
    @SerializedName(value = "id_token")
    private String idToken;
    @SerializedName(value = "token_type")
    private String tokenType;
    @SerializedName(value = "expires_in")
    private int expiresIn;

    public AccessTokenResponse() {

    }

    public AccessTokenResponse(String accessToken, String refreshToken, String idToken, String tokenType,
                               int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.idToken = idToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return "AccessTokenResponse{" + "accessToken='" + accessToken + '\'' + ", refreshToken='" + refreshToken +
                '\'' + ", idToken='" + idToken + '\'' + ", tokenType='" + tokenType + '\'' + ", expiresIn=" +
                expiresIn + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessTokenResponse)) {
            return false;
        }
        AccessTokenResponse that = (AccessTokenResponse) o;
        return expiresIn == that.expiresIn && Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(refreshToken, that.refreshToken) && Objects.equals(idToken, that.idToken) &&
                Objects.equals(tokenType, that.tokenType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken, idToken, tokenType, expiresIn);
    }
}
