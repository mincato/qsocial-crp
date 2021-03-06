package com.qsocialnow.viewmodel.team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.handler.NotificationHandler;
import com.qsocialnow.model.ListView;
import com.qsocialnow.services.TeamService;
import com.qsocialnow.services.UserResolverService;
import com.qsocialnow.services.UserService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTeamViewModel extends EditableTeamViewModel implements Serializable {

    private static final long serialVersionUID = 3526406425520096040L;

    @WireVariable
    private TeamService teamService;

    @WireVariable
    private UserService userService;

    @WireVariable
    private UserResolverService userResolverService;

    private TeamView currentTeam;

    @Init
    public void init() {
        initTeam();
        initUsers();
        initUsersResolver();
    }

    private void initUsers() {
        setUserListView(new ListView<UserListView>());
        getUserListView().setList(userService.findAll(null));
        getUserListView().setFilteredList(new ArrayList<UserListView>());
        getUserListView().getFilteredList().addAll(getUserListView().getList());
    }

    private void initUsersResolver() {
        setUserResolverListView(new ListView<UserResolverListView>());
        getUserResolverListView().setList(userResolverService.findAllActive());
        getUserResolverListView().setFilteredList(new ArrayList<UserResolverListView>());
        getUserResolverListView().getFilteredList().addAll(getUserResolverListView().getList());
    }

    private void initTeam() {
        currentTeam = new TeamView();
        currentTeam.setTeam(new Team());
        currentTeam.setUsers(new ArrayList<TeamUserView>());
        currentTeam.setUsersResolver(new ArrayList<TeamUserResolverView>());
    }

    @Command
    @NotifyChange("currentTeam")
    public void save() {
        Team team = new Team();
        team.setName(currentTeam.getTeam().getName());
        team.setActive(currentTeam.getTeam().isActive());
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
        team = teamService.create(team);
        NotificationHandler.addNotification(Labels.getLabel("team.create.notification.success",
                new String[] { team.getName() }));
        Executions.getCurrent().sendRedirect("/pages/team/list/index.zul");
    }

    @Command
    @NotifyChange({ "currentTeam", "userListView", "userResolverListView" })
    public void clear() {
        initTeam();
        initUsers();
        initUsersResolver();
    }

    public TeamView getCurrentTeam() {
        return currentTeam;
    }

    public void setCurrentTeam(TeamView currentTeam) {
        this.currentTeam = currentTeam;
    }

}