package com.qsocialnow.rest.security;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessControlAllowOriginFilter implements ContainerResponseFilter {

    @Autowired
    private AllowedOriginsHelper allowedOriginsHelper;

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext inContext, ContainerResponseContext outContext) {

        String clientOrigin = request.getHeader(SecurityConstants.CLIENT_ORIGIN_KEY);
        Map<String, String> headers = allowedOriginsHelper.getCustomAllowedOrigin(clientOrigin);

        for (Entry<String, String> entry : headers.entrySet()) {
            outContext.getHeaders().putSingle(entry.getKey(), entry.getValue());
        }
    }
}
