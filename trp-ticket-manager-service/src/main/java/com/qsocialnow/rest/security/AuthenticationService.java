package com.qsocialnow.rest.security;

import com.qsocialnow.model.user.Credential;
import com.qsocialnow.model.user.UserLogin;

public interface AuthenticationService {

    UserLogin authenticate(Credential credential);

}
