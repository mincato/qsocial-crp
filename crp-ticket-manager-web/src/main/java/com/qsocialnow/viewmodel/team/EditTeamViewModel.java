package com.qsocialnow.viewmodel.team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.services.TeamService;
import com.qsocialnow.services.UserResolverService;
import com.qsocialnow.services.UserService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditTeamViewModel implements Serializable {

    private static final long serialVersionUID = -1885863621412866685L;

    @WireVariable("mockTeamService")
    private TeamService teamService;

    @WireVariable("mockUserService")
    private UserService userService;

    @WireVariable("mockUserResolverService")
    private UserResolverService userResolverService;

    private String teamId;

    private TeamView currentTeam;

    private List<UserListView> users;

    private List<UserResolverListView> usersResolver;

    private Boolean enabledAddUser = Boolean.TRUE;

    private Boolean enabledAddUserResolver = Boolean.TRUE;

    private boolean saved;

    @Init
    public void init(@BindingParam("team") String team) {
        this.teamId = team;
        initTeam(this.teamId);
        initUsers();
        initUsersResolver();
    }

    private void initUsers() {
        this.enabledAddUser = Boolean.TRUE;
        this.users = new ArrayList<UserListView>();
        this.users.addAll(userService.findAll(null));
    }

    private void initUsersResolver() {
        this.enabledAddUserResolver = Boolean.TRUE;
        this.usersResolver = new ArrayList<UserResolverListView>();
        this.usersResolver.addAll(userResolverService.findAll(null));
    }

    private void initTeam(String teamId) {
        this.currentTeam = new TeamView();
        this.currentTeam.setTeam(teamService.findOne(teamId));
        this.currentTeam.setUsers(new ArrayList<TeamUserView>());
        this.currentTeam.setUsersResolver(new ArrayList<TeamUserResolverView>());
    }

    @Command
    @NotifyChange({ "currentTeam", "saved" })
    public void save() {
        Team team = currentTeam.getTeam();
        teamService.update(team);
        Clients.showNotification(Labels.getLabel("team.edit.notification.success", new String[] { currentTeam.getTeam()
                .getName() }));
        saved = true;
    }

    @Command
    @NotifyChange({ "currentTeam", "enabledAddUser", "enabledAddUserResolver" })
    public void clear() {
        initTeam(this.teamId);
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
    @NotifyChange({ "enabledAddUser", "users" })
    public void deleteUser(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserView deletedUser = team.getUsers().remove(idx);
        if (deletedUser.getUser() != null) {
            this.users.add(deletedUser.getUser());
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
    @NotifyChange({ "enabledAddUser", "users" })
    public void confirmUser(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserView user = team.getUsers().get(idx);
        this.users.remove(user.getUser());
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
    @NotifyChange({ "enabledAddUserResolver", "usersResolver" })
    public void deleteUserResolver(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserResolverView deletedUserResolver = team.getUsersResolver().remove(idx);
        if (deletedUserResolver.getUser() != null) {
            this.usersResolver.add(deletedUserResolver.getUser());
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
    @NotifyChange({ "enabledAddUserResolver", "usersResolver" })
    public void confirmUserResolver(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserResolverView user = team.getUsersResolver().get(idx);
        this.usersResolver.remove(user.getUser());
        user.setEditingStatus(Boolean.FALSE);
        this.enabledAddUserResolver = Boolean.TRUE;
        BindUtils.postNotifyChange(null, null, team, "usersResolver");
    }

    public boolean isSaved() {
        return saved;
    }

    public TeamView getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(TeamView currentTeam) {
        this.currentTeam = currentTeam;
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

    public Boolean getEnabledAddUserResolver() {
        return enabledAddUserResolver;
    }

    public void setEnabledAddUserResolver(Boolean enabledAddUserResolver) {
        this.enabledAddUserResolver = enabledAddUserResolver;
    }

    public List<UserListView> getUsers() {
        return users;
    }

    public void setUsers(List<UserListView> users) {
        this.users = users;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (saved) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("teamChanged", currentTeam.getTeam());
            BindUtils.postGlobalCommand(null, null, "changeTeam", args);
        }
    }

}
