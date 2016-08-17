package com.qsocialnow.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.ApplicationVersion;
import com.qsocialnow.rest.response.RestResponseHandler;
import com.qsocialnow.service.ApplicationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Service
@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "administration")
public class AdminRestService {

    @Autowired
    private RestResponseHandler responseHandler;

    @Autowired
    private ApplicationService applicationService;

    @GET
    @Path("/version")
    @ApiOperation(value = "version", notes = "Devuelve la version.", response = ApplicationVersion.class)
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Error interno del sistema => Ver code y message lanzados") })
    public Response getVersion(@Context HttpServletRequest request) {
        ApplicationVersion version = applicationService.getVersion();
        return responseHandler.buildSuccessResponse(version, Status.OK);
    }

    public void setResponseHandler(RestResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

}
