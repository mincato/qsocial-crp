package com.qsocialnow.rest.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.qsocialnow.model.user.UserData;

public class UserAuthorization implements Authentication {

    private static final long serialVersionUID = 1585427874198884286L;

    private final UserData userData;

    private boolean authenticated = true;

    private final Set<Authority> authorities;

    public UserAuthorization(UserData userData, Set<Authority> authorities) {
        this.userData = userData;
        this.authorities = authorities == null ? new HashSet<>() : authorities;
    }

    @Override
    public String getName() {
        return userData.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return userData;
    }

    @Override
    public Object getPrincipal() {
        return userData.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;

    }

}
