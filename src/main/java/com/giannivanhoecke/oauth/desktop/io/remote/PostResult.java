package com.giannivanhoecke.oauth.desktop.io.remote;

import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class PostResult {

    private final int resultCode;
    private final String resultBody;

    public PostResult(int resultCode, String resultBody) {
        this.resultCode = resultCode;
        this.resultBody = resultBody;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getContent() {
        return resultBody;
    }

    @Override
    public String toString() {
        return "PostResult{" + "resultCode=" + resultCode + ", resultBody='" + resultBody + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostResult)) {
            return false;
        }
        PostResult that = (PostResult) o;
        return resultCode == that.resultCode && Objects.equals(resultBody, that.resultBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultCode, resultBody);
    }
}
