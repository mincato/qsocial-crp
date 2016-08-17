package com.qsocialnow.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.wrapper.WrappedString;
import com.qsocialnow.model.user.Credential;
import com.qsocialnow.model.user.UserLogin;
import com.qsocialnow.rest.response.RestResponseHandler;
import com.qsocialnow.rest.security.AuthenticationService;
import com.qsocialnow.rest.security.SecurityHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Service
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "authentication")
public class AuthenticationRestService {

    @Autowired
    private RestResponseHandler responseHandler;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private SecurityHandler securityHandler;

    @POST
    @Path("/login")
    @ApiOperation(value = "login", notes = "Provee acceso a las APIs")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Error interno del sistema => Ver code y message lanzados"),
            @ApiResponse(code = 401, message = "Error de autenticacion => Ver code y message lanzados") })
    public Response login(@Context HttpServletRequest request, Credential credential) {
        UserLogin userLogin = authenticationService.authenticate(credential);
        securityHandler.configureUser(userLogin.getUser(), request);
        return responseHandler.buildSuccessResponse(userLogin, Status.OK);
    }

    @POST
    @Path("/refresh")
    public Response refreshToken(@Context HttpServletRequest request) {
        String token = securityHandler.refreshExpiredToken(request);
        return responseHandler.buildSuccessResponse(new WrappedString(token), Status.OK);
    }

    @POST
    @Path("/logout")
    @ApiOperation(value = "logout", notes = "Logout!!")
    public Response logout(@Context HttpServletRequest request) {
        return responseHandler.buildSuccessResponse(Status.OK);
    }

}
