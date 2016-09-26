package com.qsocialnow.viewmodel.team;

import java.util.Comparator;

import com.qsocialnow.common.model.config.UserListView;

public class UserListViewComparator implements Comparator<UserListView> {

    @Override
    public int compare(UserListView u1, UserListView u2) {
        return u1.getUsername().compareTo(u2.getUsername());
    }

}
