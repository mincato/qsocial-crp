package com.qsocialnow.viewmodel.team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.model.ListView;
import com.qsocialnow.services.TeamService;
import com.qsocialnow.services.UserResolverService;
import com.qsocialnow.services.UserService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditTeamViewModel extends EditableTeamViewModel implements Serializable {

    private static final long serialVersionUID = -1885863621412866685L;

    @WireVariable
    private TeamService teamService;

    @WireVariable
    private UserService userService;

    @WireVariable
    private UserResolverService userResolverService;

    private String teamId;

    private TeamView currentTeam;

    private boolean saved;

    @Init
    public void init(@BindingParam("team") String team) {
        teamId = team;
        initTeam(teamId);
        initUsers(currentTeam.getUsers());
        initUsersResolver(currentTeam.getUsersResolver());
    }

    private void initUsers(List<TeamUserView> currentUsers) {
        setUserListView(new ListView<UserListView>());
        getUserListView().setList(userService.findAll(null));
        getUserListView().setFilteredList(new ArrayList<UserListView>());
        getUserListView().getFilteredList().addAll(getUserListView().getList().stream().filter(user -> {
            boolean found = false;
            for (TeamUserView teamUserView : currentUsers) {
                if (teamUserView.getUser().getId().equals(user.getId()))
                    found = true;
            }
            return !found;
        }).collect(Collectors.toList()));
    }

    private void initUsersResolver(List<TeamUserResolverView> currentUsersResolver) {
        setUserResolverListView(new ListView<UserResolverListView>());
        getUserResolverListView().setList(userResolverService.findAll(null));
        getUserResolverListView().setFilteredList(new ArrayList<UserResolverListView>());
        getUserResolverListView().getFilteredList().addAll(
                getUserResolverListView().getList().stream().filter(user -> {
                    boolean found = false;
                    for (TeamUserResolverView teamUserResolverView : currentUsersResolver) {
                        if (teamUserResolverView.getUser().getId().equals(user.getId()))
                            found = true;
                    }
                    return !found;
                }).collect(Collectors.toList()));
    }

    private void initTeam(String teamId) {
        currentTeam = new TeamView();
        currentTeam.setTeam(teamService.findOne(teamId));
        currentTeam.setUsersResolver(currentTeam.getTeam().getUserResolvers().stream().map(userResolver -> {
            TeamUserResolverView teamUserResolver = new TeamUserResolverView();
            teamUserResolver.setUser(new UserResolverListView());
            teamUserResolver.getUser().setId(userResolver.getId());
            teamUserResolver.getUser().setIdentifier(userResolver.getIdentifier());
            teamUserResolver.getUser().setSource(userResolver.getSource());
            return teamUserResolver;
        }).collect(Collectors.toList()));
        currentTeam.setUsers(currentTeam.getTeam().getUsers().stream().map(user -> {
            TeamUserView teamUserView = new TeamUserView();
            teamUserView.setUser(new UserListView());
            teamUserView.setCoordinator(user.isCoordinator());
            teamUserView.getUser().setId(user.getId());
            teamUserView.getUser().setUsername(user.getUsername());
            return teamUserView;
        }).collect(Collectors.toList()));
    }

    @Command
    @NotifyChange({ "currentTeam", "saved" })
    public void save() {
        Team team = new Team();
        team.setId(currentTeam.getTeam().getId());
        team.setName(currentTeam.getTeam().getName());
        team.setUserResolvers(currentTeam.getUsersResolver().stream().map(userResolver -> {
            BaseUserResolver teamUserResolver = new BaseUserResolver();
            teamUserResolver.setId(userResolver.getUser().getId());
            teamUserResolver.setIdentifier(userResolver.getUser().getIdentifier());
            teamUserResolver.setSource(userResolver.getUser().getSource());
            return teamUserResolver;
        }).collect(Collectors.toList()));
        team.setUsers(currentTeam.getUsers().stream().map(userView -> {
            User user = new User();
            user.setCoordinator(userView.isCoordinator());
            user.setUsername(userView.getUser().getUsername());
            user.setId(userView.getUser().getId());
            return user;
        }).collect(Collectors.toList()));
        teamService.update(team);
        Clients.showNotification(Labels.getLabel("team.edit.notification.success", new String[] { currentTeam.getTeam()
                .getName() }));
        saved = true;
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
