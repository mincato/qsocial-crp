package com.qsocialnow.services;

import java.util.List;
import java.util.Map;

import com.qsocialnow.common.model.config.UserListView;

public interface UserService {

    List<UserListView> findAll(Map<String, String> filters);

}
