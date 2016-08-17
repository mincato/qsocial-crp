package com.qsocialnow.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.user.User;
import com.qsocialnow.persistence.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Cacheable("permissions")
    public Set<String> findPermissionsByUser(Integer userId) {
        return userRepository.findPermissionsByUser(userId);
    }

}
