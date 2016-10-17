package com.qsocialnow.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.TeamListView;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.persistence.TeamRepository;
import com.qsocialnow.persistence.UserResolverRepository;

@Service
public class TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserResolverRepository userResolverRepository;

    public Team createTeam(Team team) {
        Team teamSaved = null;
        try {
            teamSaved = teamRepository.save(team);
            if (teamSaved.getId() == null) {
                throw new Exception("There was an error creating team: " + team.getName());
            }
        } catch (Exception e) {
            log.error("There was an error creating team: " + team.getName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return teamSaved;
    }

    public PageResponse<TeamListView> findAll(Integer pageNumber, Integer pageSize, String name) {
        List<TeamListView> teams = teamRepository.findAll(new PageRequest(pageNumber, pageSize, null), name);

        PageResponse<TeamListView> page = new PageResponse<TeamListView>(teams, pageNumber, pageSize);
        return page;
    }

    public List<TeamListView> findAll() {
    	try {
	        List<TeamListView> teams = teamRepository.findAll(null, null);
	        return teams;
    	} catch (Exception e) {
    		log.error("There was an error finding teams", e);
    		throw new RuntimeException(e.getMessage());
    	}
    }

    public Team findOne(String teamId) {
        Team team = teamRepository.findOne(teamId);
        return team;
    }

    public Team update(String teamId, Team team) {
        Team teamSaved = null;
        try {
            team.setId(teamId);
            teamSaved = teamRepository.update(team);
        } catch (Exception e) {
            log.error("There was an error updating team: " + team.getName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return teamSaved;
    }

    public List<UserResolver> findUserResolvers(String teamId, Boolean status, String sourceInput) {
        List<UserResolver> userResolvers = new ArrayList<>();
        try {
            Team team = teamRepository.findOne(teamId);
            Long source = sourceInput != null ? Long.parseLong(sourceInput) : null;
            userResolvers = userResolverRepository.findUserResolvers(team.getUserResolvers(), status, source);
        } catch (Exception e) {
            log.error("There was an error getting user resolvers");
            throw new RuntimeException(e.getMessage());
        }
        return userResolvers;
    }

    public List<User> findUsers(String teamId) {
        List<User> users = new ArrayList<>();
        try {
            Team team = teamRepository.findOne(teamId);
            users = team.getUsers();
        } catch (Exception e) {
            log.error("There was an error getting user resolvers");
            throw new RuntimeException(e.getMessage());
        }
        return users;
    }

}
