package org.example.token;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenDTO {
    @JsonProperty("client_id")
    private int clientId;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("grant_type")
    private String grantType;

    public RefreshTokenDTO() {
    }


    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshTokenDTO(int clientId, String refreshToken) {
        this.clientId = clientId;
        this.refreshToken = refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    @Override
    public String toString() {
        return "RefreshTokenDTO{" +
                "clientId=" + clientId +
                ", refreshToken='" + refreshToken + '\'' +
                ", grantType='" + grantType + '\'' +
                '}';
    }
}
