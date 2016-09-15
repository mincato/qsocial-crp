package com.qsocialnow.viewmodel.team;

import java.util.List;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.UserResolverListView;

public class TeamView {

    private Team team;

    private List<TeamUserView> users;

    private List<UserResolverListView> usersResolver;

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

    public List<UserResolverListView> getUsersResolver() {
        return usersResolver;
    }

    public void setUsersResolver(List<UserResolverListView> usersResolver) {
        this.usersResolver = usersResolver;
    }

}
