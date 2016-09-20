package com.qsocialnow.services;

import com.qsocialnow.common.model.config.SourceCredentials;
import com.qsocialnow.common.model.config.UserResolver;

public interface OauthService {

    String getAuthorizationUrl();

    SourceCredentials getCredentials();

    void resolveCredentials(UserResolver userResolver);

}
