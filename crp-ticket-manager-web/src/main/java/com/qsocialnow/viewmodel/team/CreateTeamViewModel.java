package com.qsocialnow.viewmodel.team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.services.TeamService;
import com.qsocialnow.services.UserResolverService;
import com.qsocialnow.services.UserService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTeamViewModel implements Serializable {

    private static final long serialVersionUID = 3526406425520096040L;

    @WireVariable("mockTeamService")
    private TeamService teamService;

    @WireVariable("mockUserService")
    private UserService userService;

    @WireVariable("mockUserResolverService")
    private UserResolverService userResolverService;

    private TeamView currentTeam;

    private List<UserListView> users;

    private List<UserResolverListView> usersResolver;

    @Init
    public void init() {
        initTeam();
        initUsers();
        initUsersResolver();
    }

    private void initUsers() {
        users = new ArrayList<UserListView>();
        users.addAll(userService.findAll(null));
    }

    private void initUsersResolver() {
        usersResolver = new ArrayList<UserResolverListView>();
        usersResolver.addAll(userResolverService.findAll(null));
    }

    private void initTeam() {
        currentTeam = new TeamView();
        currentTeam.setTeam(new Team());
        currentTeam.setUsers(new ArrayList<TeamUserView>());
        currentTeam.setUsersResolver(new ArrayList<UserResolverListView>());
    }

    @Command
    @NotifyChange("currentTeam")
    public void save() {
        Team team = new Team();
        team.setName(currentTeam.getTeam().getName());
        team = teamService.create(team);
        Clients.showNotification(Labels.getLabel("team.create.notification.success", new String[] { team.getName() }));
        initTeam();
    }

    @Command
    @NotifyChange({ "currentTeam" })
    public void clear() {
        initTeam();
    }

    public TeamView getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(TeamView currentTeam) {
        this.currentTeam = currentTeam;
    }

    public List<UserListView> getUsers() {
        return users;
    }

    public void setUsers(List<UserListView> users) {
        this.users = users;
    }

    public List<UserResolverListView> getUsersResolver() {
        return usersResolver;
    }

    public void setUsersResolver(List<UserResolverListView> usersResolver) {
        this.usersResolver = usersResolver;
    }

    @Command
    public void addUser(@BindingParam("fx") TeamView team) {
        team.getUsers().add(new TeamUserView());
        BindUtils.postNotifyChange(null, null, team, "users");
    }

    @Command
    public void addUserResolver(@BindingParam("fx") TeamView team) {
        team.getUsersResolver().add(new UserResolverListView());
        BindUtils.postNotifyChange(null, null, team, "usersResolver");
    }
}