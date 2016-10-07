package com.qsocialnow.services;

import com.qsocialnow.common.model.config.UserResolver;

public interface OauthService {

    String getAuthorizationUrl();

    void resolveCredentials(UserResolver userResolver);

}
