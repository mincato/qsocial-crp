package com.qsocialnow.rest.security;

import java.util.Set;

import com.qsocialnow.model.user.User;

public class UserDetailsFactory {

    public static UserDetails create(User user, Set<Authority> authorities) {
        UserDetails userDetails = new UserDetails(user, authorities);
        return userDetails;
    }

}
