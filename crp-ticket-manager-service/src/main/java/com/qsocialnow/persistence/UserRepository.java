package com.qsocialnow.persistence;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.NotFoundException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.user.User;

@Service
public class UserRepository {

    private static Map<Integer, User> users;

    private static Map<Integer, Set<String>> permissionsByUser;

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class);

    static {
        User adminUser = UserBuilder.createAdmin();

        users = new ConcurrentHashMap<Integer, User>();
        users.put(adminUser.getId(), adminUser);

        Set<String> permissions = UserBuilder.createDeveloperPermissions();
        permissions.addAll(UserBuilder.createAdminPermissions());

        permissionsByUser = new ConcurrentHashMap<Integer, Set<String>>();
        permissionsByUser.put(adminUser.getId(), permissions);

        LOGGER.info("============ " + users.size() + " created");
    }

    public User findByUsername(String username) {
        Optional<User> userOptional = users.values().stream().filter(user -> username.equals(user.getUsername()))
                .findFirst();
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException();
        }
    }

    public Set<String> findPermissionsByUser(Integer userId) {
        return permissionsByUser.get(userId);
    }
}
