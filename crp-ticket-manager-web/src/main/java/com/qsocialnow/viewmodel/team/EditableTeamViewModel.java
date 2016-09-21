package com.qsocialnow.viewmodel.team;

import java.util.Collections;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.config.UserResolverListView;

public class EditableTeamViewModel {

    private TeamListView<UserListView> userListView;

    private TeamListView<UserResolverListView> userResolverListView;

    @Command
    @NotifyChange({ "userListView" })
    public void addUser(@BindingParam("fx") TeamView team) {
        TeamUserView user = new TeamUserView();
        user.setEditingStatus(Boolean.TRUE);
        team.getUsers().add(user);
        userListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, team, "users");
    }

    @Command
    @NotifyChange({ "userListView" })
    public void deleteUser(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserView deletedUser = team.getUsers().remove(idx);
        if (!deletedUser.isEditingStatus()) {
            addUserFilteredList(userListView, deletedUser.getUser());
        }
        userListView.setEnabledAdd(true);
        for (TeamUserView user : team.getUsers()) {
            if (user.isEditingStatus()) {
                userListView.setEnabledAdd(false);
            }
        }
        BindUtils.postNotifyChange(null, null, team, "users");

    }

    private void addUserFilteredList(TeamListView<UserListView> teamUserListView, UserListView user) {
        if (user == null) {
            return;
        }
        for (UserListView userListView : userListView.getList()) {
            if (userListView.getId().equals(user.getId())) {
                teamUserListView.getFilteredList().add(userListView);
            }
        }
        Collections.sort(userListView.getFilteredList(), new UserListViewComparator());
    }

    private void deleteUserFilteredList(TeamListView<UserListView> teamUserListView, UserListView user) {
        if (user == null) {
            return;
        }
        teamUserListView.getFilteredList().remove(user);
    }

    private void deleteUserResolverFilteredList(TeamListView<UserResolverListView> teamUserResolverListView,
            UserResolverListView user) {
        if (user == null) {
            return;
        }
        teamUserResolverListView.getFilteredList().remove(user);
    }

    @Command
    @NotifyChange({ "userListView" })
    public void confirmUser(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserView user = team.getUsers().get(idx);
        user.setEditingStatus(Boolean.FALSE);
        deleteUserFilteredList(userListView, user.getUser());
        userListView.setEnabledAdd(true);
        BindUtils.postNotifyChange(null, null, team, "users");

    }

    @Command
    @NotifyChange({ "userResolverListView" })
    public void addUserResolver(@BindingParam("fx") TeamView team) {
        TeamUserResolverView user = new TeamUserResolverView();
        user.setEditingStatus(true);
        team.getUsersResolver().add(user);
        userResolverListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, team, "usersResolver");

    }

    @Command
    @NotifyChange({ "userResolverListView" })
    public void deleteUserResolver(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserResolverView deletedUser = team.getUsersResolver().remove(idx);
        if (!deletedUser.isEditingStatus()) {
            addUserFilteredList(userResolverListView, deletedUser.getUser());
        }
        userResolverListView.setEnabledAdd(true);
        for (TeamUserResolverView user : team.getUsersResolver()) {
            if (user.isEditingStatus()) {
                userResolverListView.setEnabledAdd(false);
            }
        }
        BindUtils.postNotifyChange(null, null, team, "usersResolver");
    }

    private void addUserFilteredList(TeamListView<UserResolverListView> teamUserResolverListView,
            UserResolverListView user) {
        if (user == null) {
            return;
        }
        for (UserResolverListView userResolverListView : teamUserResolverListView.getList()) {
            if (userResolverListView.getId().equals(user.getId())) {
                teamUserResolverListView.getFilteredList().add(userResolverListView);
            }
        }
        Collections.sort(teamUserResolverListView.getFilteredList(), new UserResolverListViewComparator());

    }

    @Command
    @NotifyChange({ "userResolverListView" })
    public void confirmUserResolver(@BindingParam("index") int idx, @BindingParam("fx") TeamView team) {
        TeamUserResolverView user = team.getUsersResolver().get(idx);
        user.setEditingStatus(Boolean.FALSE);
        deleteUserResolverFilteredList(userResolverListView, user.getUser());
        userResolverListView.setEnabledAdd(true);
        BindUtils.postNotifyChange(null, null, team, "usersResolver");
    }

    public TeamListView<UserListView> getUserListView() {
        return userListView;
    }

    public void setUserListView(TeamListView<UserListView> userListView) {
        this.userListView = userListView;
    }

    public TeamListView<UserResolverListView> getUserResolverListView() {
        return userResolverListView;
    }

    public void setUserResolverListView(TeamListView<UserResolverListView> userResolverListView) {
        this.userResolverListView = userResolverListView;
    }

}
