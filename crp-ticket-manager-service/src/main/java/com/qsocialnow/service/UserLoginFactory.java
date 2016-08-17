package com.qsocialnow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.user.User;
import com.qsocialnow.model.user.UserLogin;

@Service
public class UserLoginFactory {

    @Autowired
    private UserService userService;

    public UserLogin create(User user) {
        UserLogin userLogin = new UserLogin();
        userLogin.setUser(user);
        userLogin.setPermissions(userService.findPermissionsByUser(user.getId()));
        return userLogin;
    }

}
