package com.ctw.audit.model.bean;

import com.ctw.audit.model.entity.User;
import com.ctw.audit.model.entity.UserCredentials;
import com.ctw.audit.model.entity.UserRoles;
import com.ctw.audit.model.enumaration.RolesEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@SuppressWarnings("WeakerAccess")
@JsonInclude(NON_NULL)
public class UserDetailsImpl implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String id;
    private String username;
    private String password;
    private String schoolId;
    private Set<GrantedAuthority> authorities;

    public UserDetailsImpl(UserCredentials userCredentials, User user) {
        this.id = userCredentials.getId();
        this.username = userCredentials.getUsername();
        this.password = userCredentials.getPassword();
        this.setAuthorities(user.getRoles());
    }

    private void setAuthorities(List<UserRoles> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>(roles.size());
        for (UserRoles userRole : roles) {
            String role = userRole.getRole().name();
            Assert.isTrue(!role.startsWith("ROLE_"), role
                    + " cannot start with ROLE_ (it is automatically added)");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        this.setAuthorities(authorities);
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void eraseCredentials() {
        password = null;
    }

    @SuppressWarnings("unused")
    public List<String> getRoles(){
        if(authorities == null || authorities.isEmpty()){
            return new ArrayList<>();
        }
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof UserDetailsImpl) {
            return username.equals(((UserDetailsImpl) rhs).username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");

        if (!CollectionUtils.isEmpty(authorities)) {
            sb.append("Granted Authorities: ");

            for (GrantedAuthority auth : authorities) {
                sb.append(',').append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }
        return sb.toString();
    }
}
