package com.qsocialnow.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.qsocialnow.rest.response.RestResponseHandler;
import com.qsocialnow.service.CacheService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Service
@Path("/cache")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "cache", authorizations = { @Authorization(value = "bearer") })
public class CacheRestService {

    @Autowired
    private RestResponseHandler responseHandler;

    @Autowired
    private CacheService cacheService;

    @PUT
    @Path("clean")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "clean", notes = "Limpia la cache.")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Error interno del sistema => Ver code y message lanzados"),
            @ApiResponse(code = 401, message = "Usuario no autenticado"),
            @ApiResponse(code = 403, message = "Usuario no autorizado") })
    public Response clean(@Context HttpServletRequest request) {
        cacheService.clean();
        return responseHandler.buildSuccessResponse(Status.OK);
    }

    public void setResponseHandler(RestResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

}
