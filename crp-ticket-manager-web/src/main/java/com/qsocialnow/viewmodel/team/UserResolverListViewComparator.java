package com.qsocialnow.viewmodel.team;

import java.util.Comparator;

import com.qsocialnow.common.model.config.UserResolverListView;

public class UserResolverListViewComparator implements Comparator<UserResolverListView> {

    @Override
    public int compare(UserResolverListView u1, UserResolverListView u2) {
        return u1.getIdentifier().compareTo(u2.getIdentifier());
    }

}
