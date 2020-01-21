package com.ctw.audit.model.entity;

import javax.persistence.*;

@Entity
@SuppressWarnings("unused")
@Table(name = "credentials")
public class UserCredentials {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public UserCredentials() {
    }

    public UserCredentials(User user, String password) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @SuppressWarnings("unused")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }

}