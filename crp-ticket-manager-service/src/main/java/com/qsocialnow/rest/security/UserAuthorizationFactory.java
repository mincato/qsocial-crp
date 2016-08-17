package com.qsocialnow.rest.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.user.UserData;

@Service
public class UserAuthorizationFactory {

    @Autowired
    private RoleManager roleManager;

    public UserAuthorization build(UserData userData) {
        UserAuthorization userAuthentication = null;
        if (userData != null) {
            Set<Authority> roles = roleManager.get(userData.getId());
            userAuthentication = new UserAuthorization(userData, roles);
        }
        return userAuthentication;
    }

}
