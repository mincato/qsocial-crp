package com.qsocialnow.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.persistence.UserRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<UserListView> findAll() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("There was an error finding users", e);
            throw new RuntimeException(e);
        }
    }

}
