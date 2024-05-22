package org.example.authorization;

public class Authentication {
    private String responseType;
    private long clientId;
    private String redirectUri;
    private String state;
    private String scope;

    public Authentication(){

    }

    public Authentication(String responseType, long clientId, String redirectUri, String state, String scope) {
        this.responseType = responseType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.state = state;
        this.scope = scope;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "responseType=" + responseType +
                ", clientId=" + clientId +
                ", redirectUri='" + redirectUri + '\'' +
                ", state='" + state + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
