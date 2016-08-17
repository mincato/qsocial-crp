package com.qsocialnow.persistence;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.qsocialnow.common.random.RandomInteger;
import com.qsocialnow.model.user.User;

public class UserBuilder {

    public static User createAdmin() {
        User user = new User();
        user.setId(RandomInteger.getNext());
        user.setUsername("admin");
        user.setPassword(new BCryptPasswordEncoder().encode("admin"));
        user.setFirstName("Adrian");
        user.setLastName("Paredes");
        return user;
    }

    public static Set<String> createDeveloperPermissions() {
        Set<String> permissions = new HashSet<>();
        permissions.add("READ_DEVELOPER");
        permissions.add("CREATE_DEVELOPER");
        permissions.add("UPDATE_DEVELOPER");
        permissions.add("DELETE_DEVELOPER");
        return permissions;
    }

    public static Set<String> createAdminPermissions() {
        Set<String> permissions = new HashSet<>();
        permissions.add("ADMIN");
        return permissions;
    }

}
