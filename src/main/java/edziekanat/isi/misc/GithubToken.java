package edziekanat.isi.misc;

public class GithubToken {
    private String accessToken;
    private String tokenType;
    private String scope;
    private String error;
    private String errorDescription;

    public GithubToken(String accessToken, String tokenType, String scope, String error, String errorDescription) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}