package com.qsocialnow.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.user.Credential;
import com.qsocialnow.model.user.UserLogin;
import com.qsocialnow.service.UserLoginFactory;

@Service
public class SpringAuthenticationService implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserLoginFactory userLoginFactory;

    @Override
    public UserLogin authenticate(Credential credential) {
        try {
            Authentication authentication = this.authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(credential.getUsername(), credential
                            .getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userLoginFactory.create(userDetails.getUser());
        } catch (AuthenticationException ex) {
            throw new com.qsocialnow.common.exception.AuthenticationException(ex);
        }
    }

}
