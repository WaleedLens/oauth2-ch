package org.example.token;

import org.example.annotations.TableEntity;
import org.example.database.Identifiable;

import java.sql.Timestamp;

@TableEntity(tableName = "tokens")
public class Token implements Identifiable<Long> {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private Timestamp createdAt;

    public Token() {
    }



    public Token(String accessToken,String refreshToken, int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;

    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public void setId(Long id) {

    }


    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", createdAt=" + createdAt +
                '}';
    }
}
