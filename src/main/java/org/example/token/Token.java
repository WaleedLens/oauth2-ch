package org.example.token;

import org.example.annotations.TableEntity;
import org.example.database.Identifiable;

@TableEntity(tableName = "tokens")
public class Token implements Identifiable<Long> {
    private String accessToken;
    private int expiresIn;
    private Long id;

    public Token() {
    }

    public Token(String accessToken, int expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
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

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public void setId(Long id) {

    }
}
