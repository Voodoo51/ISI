package edziekanat.isi.dto;

public class GithubToken {
    private String access_token;
    private String token_type;
    private String scope;
    private String error;
    private String error_description;

    public GithubToken(String access_token, String token_type, String scope, String error, String error_description) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.scope = scope;
        this.error = error;
        this.error_description = error_description;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
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

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}