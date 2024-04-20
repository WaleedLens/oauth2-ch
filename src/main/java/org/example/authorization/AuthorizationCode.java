package org.example.authorization;

import org.example.annotations.TableEntity;
import org.example.database.Identifiable;

import java.util.Date;

@TableEntity(tableName = "authorization_code")
public class AuthorizationCode implements Identifiable<Long> {
    private byte[] code;
    private long clientId;
    private  String redirectUri;
    private String scope;
    private Date createdDate;
    private Date expiryDate;
    private Long id;

    public AuthorizationCode(byte[] code, long clientId, String redirectUri, String scope, Date expiryDate) {
        this.code = code;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.createdDate = new Date();
        this.expiryDate = expiryDate;
    }

    public AuthorizationCode() {
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public byte[] getCode() {
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
}
