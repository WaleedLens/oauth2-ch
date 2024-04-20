package org.example.clientAuth;

import org.example.annotations.TableEntity;
import org.example.database.Identifiable;

import java.util.Objects;
import java.util.UUID;


@TableEntity(tableName = "clients_auth")
public class ClientAuth implements Identifiable<Long> {
    private Long id;
    private String clientUid; //UUID generated unique id
    private String clientSecret; //UUID generated unique secret
    private Long clientId; //Client id from client table


    public ClientAuth() {
    }

    public ClientAuth(String clientUid, String clientSecret, Long clientId) {
        this.clientUid = clientUid;
        this.clientSecret = clientSecret;
        this.clientId = clientId;

    }

    public String getClientUid() {
        return clientUid;
    }

    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientAuth that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getClientUid(), that.getClientUid()) && Objects.equals(getClientSecret(), that.getClientSecret()) && Objects.equals(getClientId(), that.getClientId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientUid(), getClientSecret(), getClientId());
    }

    @Override
    public String toString() {
        return "ClientAuth{" +
                "id=" + id +
                ", clientUid='" + clientUid + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}
