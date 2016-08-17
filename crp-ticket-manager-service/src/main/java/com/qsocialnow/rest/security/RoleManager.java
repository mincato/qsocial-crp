package com.qsocialnow.rest.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.service.UserService;

@Service
public class RoleManager {

    @Autowired
    private UserService userService;

    public Set<Authority> get(Integer userId) {
        Set<Authority> authorities = null;
        Set<String> permissions = userService.findPermissionsByUser(userId);
        if (permissions != null) {
            authorities = permissions.stream().map(permission -> new Authority(permission)).collect(Collectors.toSet());
        }
        return authorities;
    }

}
