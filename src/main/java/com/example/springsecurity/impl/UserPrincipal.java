package com.example.springsecurity.impl;

import com.example.springsecurity.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class UserPrincipal implements UserDetails {
    private UUID userId;
    private String username;
    private String firstName;
    private String lastName;
    private String encryptedPassword;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor to copy properties from UserEntity
    public UserPrincipal(UserEntity userEntity) {
        this.userId = userEntity.getId();
        this.username = userEntity.getUsername();
        this.encryptedPassword = userEntity.getEncryptedPassword();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
    }

    // Implement required methods from UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return encryptedPassword;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Additional method to get userId
    public UUID getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }
}
