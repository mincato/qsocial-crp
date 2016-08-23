package com.qsocialnow.rest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.pagination.PageResponse;
import com.qsocialnow.rest.response.RestResponseHandler;
import com.qsocialnow.service.DomainService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Service
@Path("/domains")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "domains")
public class DomainRestService {

    @Autowired
    private RestResponseHandler responseHandler;

    @Autowired
    private DomainService domainService;

    @GET
    @ApiOperation(value = "findAll", notes = "Busca todos los domains.", response = Domain.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Error interno del sistema => Ver code y message lanzados"),
            @ApiResponse(code = 401, message = "Usuario no autenticado"),
            @ApiResponse(code = 403, message = "Usuario no autorizado") })
    public Response findAll(@Context HttpServletRequest request, @QueryParam("pageNumber") Integer pageNumber,
            @QueryParam("pageSize") Integer pageSize) {
        PageResponse<DomainListView> page = domainService.findAll(pageNumber, pageSize);
        return responseHandler.buildSuccessResponse(page, Status.OK);
    }

    @POST
    @ApiOperation(value = "save", notes = "Crea un nuevo domain.", response = Domain.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Error interno del sistema => Ver code y message lanzados"),
            @ApiResponse(code = 401, message = "Usuario no autenticado"),
            @ApiResponse(code = 403, message = "Usuario no autorizado") })
    public Response save(@Context HttpServletRequest request, @Valid Domain newDomain) {
        Domain domainSaved = domainService.save(newDomain);
        return responseHandler.buildSuccessResponse(domainSaved, Status.OK);
    }

    public void setResponseHandler(RestResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
