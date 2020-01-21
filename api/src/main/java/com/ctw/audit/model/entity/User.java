package com.ctw.audit.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Entity
@Audited
@JsonInclude(NON_NULL)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    private String name;

    @OneToMany
    @JoinColumn(name="user_id")
    @SuppressWarnings("JpaDataSourceORMInspection")
    private List<UserRoles> roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserRoles> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoles> roles) {
        this.roles = roles;
    }
}