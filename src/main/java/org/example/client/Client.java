package org.example.client;

import org.example.database.Identifiable;
import org.example.annotations.TableEntity;

import java.sql.Timestamp;
import java.util.Objects;

@TableEntity(tableName = "clients")
public class Client implements Identifiable<Long> {
    private Long id;
    private String name;
    private String website;
    private String logo;
    private String redirectUri;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Client() {

    }

    public Client(String name, String website, String logo, String redirectUri) {
        this.name = name;
        this.website = website;
        this.logo = logo;
        this.redirectUri = redirectUri;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectURI(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Objects.equals(getId(), client.getId()) && Objects.equals(getName(), client.getName()) && Objects.equals(getWebsite(), client.getWebsite()) && Objects.equals(getLogo(), client.getLogo()) && Objects.equals(getRedirectUri(), client.getRedirectUri()) && Objects.equals(getCreatedAt(), client.getCreatedAt()) && Objects.equals(getUpdatedAt(), client.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getWebsite(), getLogo(), getRedirectUri(), getCreatedAt(), getUpdatedAt());
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", logo='" + logo + '\'' +
                ", redirectURI='" + redirectUri + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
