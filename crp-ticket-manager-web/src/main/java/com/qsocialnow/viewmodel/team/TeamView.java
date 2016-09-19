package com.qsocialnow.viewmodel.team;

import java.util.List;

import javax.validation.Valid;

import com.qsocialnow.common.model.config.Team;

public class TeamView {

    @Valid
    private Team team;

    private List<TeamUserView> users;

    private List<TeamUserResolverView> usersResolver;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<TeamUserView> getUsers() {
        return users;
    }

    public void setUsers(List<TeamUserView> users) {
        this.users = users;
    }

    public List<TeamUserResolverView> getUsersResolver() {
        return usersResolver;
    }

    public void setUsersResolver(List<TeamUserResolverView> usersResolver) {
        this.usersResolver = usersResolver;
    }

}
