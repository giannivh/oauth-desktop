package com.giannivanhoecke.oauth.desktop.io.remote;

import java.util.Objects;

/**
 * @author gvhoecke {@literal <gianni@giannivanhoecke.com>}
 * @since 1.0
 */
public class GetResult {

    private final int resultCode;
    private final String content;

    public GetResult(int resultCode, String content) {
        this.resultCode = resultCode;
        this.content = content;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "GetResult{" + "resultCode=" + resultCode + ", content='" + content + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GetResult)) {
            return false;
        }
        GetResult getResult = (GetResult) o;
        return resultCode == getResult.resultCode && Objects.equals(content, getResult.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultCode, content);
    }
}
