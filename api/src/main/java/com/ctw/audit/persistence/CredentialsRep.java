package com.ctw.audit.persistence;

import com.ctw.audit.model.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRep extends JpaRepository<UserCredentials, String> {

//    @Query("select uc from UserCredentials uc where uc.user.username = :username")
    UserCredentials findByUsername(String username);
}