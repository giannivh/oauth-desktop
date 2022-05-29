package com.giannivanhoecke.oauth.desktop.representation;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class UserInfoResponse {

    @SerializedName(value = "sub")
    private String id;
    @SerializedName(value = "preferred_username")
    private String username;
    @SerializedName(value = "name")
    private String name;
    @SerializedName(value = "email")
    private String email;

    public UserInfoResponse() {

    }

    public UserInfoResponse(String id, String username, String name, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "OAuth2UserInfo{" + "id='" + id + '\'' + ", username='" + username + '\'' + ", name='" + name + '\'' +
                ", email='" + email + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInfoResponse)) {
            return false;
        }
        UserInfoResponse that = (UserInfoResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) &&
                Objects.equals(name, that.name) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, name, email);
    }
}
