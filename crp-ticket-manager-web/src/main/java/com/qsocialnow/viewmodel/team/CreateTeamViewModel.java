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

    private List<UserListView> filteredUsers;

    private List<UserResolverListView> usersResolver;

    private List<UserResolverListView> filteredUsersResolver;

    private Boolean enabledAddUser = Boolean.TRUE;

    private Boolean enabledAddUserResolver = Boolean.TRUE;

    private TeamUserView teamUserView;

    @Init
    public void init() {
        initTeam();
        initUsers();
        initUsersResolver();
    }
    
    private void initUsers() {
        this.enabledAddUser = Boolean.TRUE;
        this.users = new ArrayList<UserListView>();
        this.users.addAll(userService.findAll(null));
        this.filteredUsers = new ArrayList<UserListView>();
        this.filteredUsers.addAll(users);
    }

    private void initUsersResolver() {
        this.enabledAddUserResolver = Boolean.TRUE;
        this.usersResolver = new ArrayList<UserResolverListView>();
        this.usersResolver.addAll(userResolverService.findAll(null));
        this.filteredUsersResolver = new ArrayList<UserResolverListView>();
        this.filteredUsersResolver.addAll(usersResolver);
    }

    private void initTeam() {
        this.currentTeam = new TeamView();
        this.currentTeam.setTeam(new Team());
        this.currentTeam.setUsers(new ArrayList<TeamUserView>());
        this.currentTeam.setUsersResolver(new ArrayList<TeamUserResolverView>());
    }

    @Command
    @NotifyChange("currentTeam")
    public void save() {
        Team team = new Team();
        team.setName(currentTeam.getTeam().getName());
        team = teamService.create(team);
        Clients.showNotification(Labels.getLabel("team.create.notification.success", new String[] { team.getName() }));
        initTeam();
        initUsers();
        initUsersResolver();
    }

    @Command
    @NotifyChange({ "currentTeam", "enabledAddUser", "enabledAddUserResolver" })
    public void clear() {
        initTeam();
        initUsers();
        initUsersResolver();
    }

    @Command
    @NotifyChange({ "enabledAddUser" })
    public void addUser(@BindingParam("fx") TeamView team) {
        TeamUserView user = new TeamUserView();
        user.setEditingStatus(Boolean.TRUE);
        team.getUsers().add(user);
        this.enabledAddUser = Boolean.FALSE;
        BindUtils.postNotifyChange(null, null, team, "users");
    }

    @Command
    @NotifyChange({ "enabledAddUser", "filteredUsers" })
    public void deleteUser(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserView deletedUser = team.getUsers().remove(idx);
        if (deletedUser.getUser() != null) {
            this.filteredUsers.add(deletedUser.getUser());
        }
        this.enabledAddUser = Boolean.TRUE;
        for (TeamUserView user : team.getUsers()) {
            if (user.getEditingStatus()) {
                this.enabledAddUser = Boolean.FALSE;
            }
        }
        BindUtils.postNotifyChange(null, null, team, "users");
    }

    @Command
    @NotifyChange({ "enabledAddUser", "filteredUsers" })
    public void confirmUser(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserView user = team.getUsers().get(idx);
        this.filteredUsers.remove(user.getUser());
        user.setEditingStatus(Boolean.FALSE);
        this.enabledAddUser = Boolean.TRUE;
        BindUtils.postNotifyChange(null, null, team, "users");
    }

    @Command
    @NotifyChange({ "enabledAddUserResolver" })
    public void addUserResolver(@BindingParam("fx") TeamView team) {
        TeamUserResolverView user = new TeamUserResolverView();
        user.setEditingStatus(Boolean.TRUE);
        team.getUsersResolver().add(user);
        this.enabledAddUserResolver = Boolean.FALSE;
        BindUtils.postNotifyChange(null, null, team, "usersResolver");
    }

    @Command
    @NotifyChange({ "enabledAddUserResolver", "filteredUsersResolver" })
    public void deleteUserResolver(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserResolverView deletedUserResolver = team.getUsersResolver().remove(idx);
        if (deletedUserResolver.getUser() != null) {
            this.filteredUsersResolver.add(deletedUserResolver.getUser());
        }
        this.enabledAddUserResolver = Boolean.TRUE;
        for (TeamUserResolverView user : team.getUsersResolver()) {
            if (user.getEditingStatus()) {
                this.enabledAddUserResolver = Boolean.FALSE;
            }
        }
        BindUtils.postNotifyChange(null, null, team, "usersResolver");
    }

    @Command
    @NotifyChange({ "enabledAddUserResolver", "filteredUsersResolver" })
    public void confirmUserResolver(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserResolverView user = team.getUsersResolver().get(idx);
        this.filteredUsersResolver.remove(user.getUser());
        user.setEditingStatus(Boolean.FALSE);
        this.enabledAddUserResolver = Boolean.TRUE;
        BindUtils.postNotifyChange(null, null, team, "usersResolver");
    }

    public TeamView getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(TeamView currentTeam) {
        this.currentTeam = currentTeam;
    }

    public List<UserListView> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<UserListView> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public List<UserResolverListView> getUsersResolver() {
        return usersResolver;
    }

    public void setUsersResolver(List<UserResolverListView> usersResolver) {
        this.usersResolver = usersResolver;
    }

    public Boolean getEnabledAddUser() {
        return enabledAddUser;
    }

    public void setEnabledAddUser(Boolean enabledAddUser) {
        this.enabledAddUser = enabledAddUser;
    }

    public TeamUserView getTeamUserView() {
        return teamUserView;
    }

    public void setTeamUserView(TeamUserView teamUserView) {
        this.teamUserView = teamUserView;
    }

    public Boolean getEnabledAddUserResolver() {
        return enabledAddUserResolver;
    }

    public void setEnabledAddUserResolver(Boolean enabledAddUserResolver) {
        this.enabledAddUserResolver = enabledAddUserResolver;
    }

    public List<UserResolverListView> getFilteredUsersResolver() {
        return filteredUsersResolver;
    }

    public void setFilteredUsersResolver(List<UserResolverListView> filteredUsersResolver) {
        this.filteredUsersResolver = filteredUsersResolver;
    }

}