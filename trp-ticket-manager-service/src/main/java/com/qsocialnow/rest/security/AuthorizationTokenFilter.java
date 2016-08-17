package com.qsocialnow.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.qsocialnow.model.user.UserData;

public class AuthorizationTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private SecurityHandler requestHandler;

    @Autowired
    private UserAuthorizationFactory userAuthenticationFactory;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        UserData userData;
        try {
            userData = this.requestHandler.verifyToken(httpRequest);
        } catch (Exception ex) {
            httpRequest.setAttribute(SecurityConstants.AUTHORIZATION_EXCEPTION, ex);
            userData = null;
        }

        Authentication authentication = userAuthenticationFactory.build(userData);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

}
