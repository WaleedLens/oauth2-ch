package org.example.authorization;

import org.example.annotations.TableEntity;
import org.example.database.Identifiable;
import org.example.token.TokenDTO;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@TableEntity(tableName = "authorization_code")
public class AuthorizationCode implements Identifiable<Long> {
    private String code;
    private long clientId;
    private String redirectUri;
    private String scope;
    private Date createdDate;
    private Date expiryDate;
    private Long id;


    public AuthorizationCode() {
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }


    public void setExpirationDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpirationDate() {
        return expiryDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        TokenDTO that = (TokenDTO) o;
        return clientId == Long.valueOf(that.getClientId()) && Objects.equals(code, that.getCode()) && Objects.equals(redirectUri, that.getRedirectUri()) && Objects.equals(scope, that.getGrantType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, clientId, redirectUri, scope);
    }

    @Override
    public String toString() {
        return "AuthorizationCode{" +
                "code=" + code +
                ", clientId=" + clientId +
                ", redirectUri='" + redirectUri + '\'' +
                ", scope='" + scope + '\'' +
                ", createdDate=" + createdDate +
                ", expiryDate=" + expiryDate +
                ", id=" + id +
                '}';
    }
}
