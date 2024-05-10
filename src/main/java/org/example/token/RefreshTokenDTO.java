package org.example.token;

public class RefreshTokenDTO {
    private int clientId;
    private String refreshToken;

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

    @Override
    public String toString() {
        return "RefreshTokenDTO{" +
                "clientId=" + clientId +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
