package com.qsocialnow.persistence;

import java.util.List;

import com.qsocialnow.common.model.config.UserListView;

public interface UserRepository {

    List<UserListView> findAll();

}
