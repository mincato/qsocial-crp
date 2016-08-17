package com.qsocialnow.rest.security;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.exception.UnauthorizedException;
import com.qsocialnow.rest.json.JSONObjectConverter;
import com.qsocialnow.rest.response.BackEndError;
import com.qsocialnow.rest.response.BackEndErrorFactory;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    @Autowired
    private AllowedOriginsHelper allowedOriginsHelper;

    @Autowired
    private BackEndErrorFactory errorFactory;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {

        String clientOrigin = request.getHeader(SecurityConstants.CLIENT_ORIGIN_KEY);
        Map<String, String> headers = allowedOriginsHelper.getCustomAllowedOrigin(clientOrigin);

        for (Entry<String, String> entry : headers.entrySet()) {
            response.setHeader(entry.getKey(), entry.getValue());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType(MediaType.APPLICATION_JSON);
        response.getWriter().write(buildJSONError(request));
    }

    private String buildJSONError(HttpServletRequest httpServletRequest) {
        Exception authException = (Exception) httpServletRequest
                .getAttribute(SecurityConstants.AUTHORIZATION_EXCEPTION);
        if (authException == null) {
            authException = new UnauthorizedException();
        }

        BackEndError internalError = errorFactory.buildInternalError(authException);
        return JSONObjectConverter.convertToJSON(internalError);
    }

}
