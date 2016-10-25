package com.qsocialnow.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.persistence.TeamRepository;
import com.qsocialnow.persistence.UserRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    public List<UserListView> findAll() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("There was an error finding users", e);
            throw new RuntimeException(e);
        }
    }

    public List<UserListView> findAllByUserName(String userName) {
        try {
            List<Team> teams = teamRepository.findTeams(userName);
            Set<User> usersFromTeam = new HashSet<User>();
            if (teams != null) {
                for (Team team : teams) {
                    List<User> users = team.getUsers();
                    for (User user : users) {
                        if (user.getUsername().equals(userName)) {
                            if (user.isCoordinator()) {
                                usersFromTeam.addAll(team.getUsers());
                                break;
                            }
                        }
                    }
                }
            }
            List<UserListView> usersView = new ArrayList<>();
            for (User user : usersFromTeam) {
                UserListView userView = new UserListView();
                userView.setId(user.getId());
                userView.setUsername(user.getUsername());
                usersView.add(userView);
            }
            return usersView;
        } catch (Exception e) {
            log.error("There was an error finding users", e);
            throw new RuntimeException(e);
        }
    }
}
