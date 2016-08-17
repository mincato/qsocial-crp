package com.qsocialnow.rest.response;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestResponseHandler {

    @Autowired
    private BackEndErrorFactory errorFactory;

    private static final Logger LOGGER = Logger.getLogger(RestResponseHandler.class);

    public Response buildSuccessResponse(Object entity, StatusType successStatus) {
        return Response.status(successStatus).entity(entity).build();
    }

    public Response buildSuccessResponse(StatusType successStatus) {
        return buildSuccessResponse(null, successStatus);
    }

    public Response buildErrorResponse(Exception exc) {
        return buildErrorResponse(exc, true);
    }

    public Response buildErrorResponse(Exception exc, boolean logException) {

        if (logException) {
            LOGGER.error("Failed!", exc);
        }

        BackEndError internalError = errorFactory.buildInternalError(exc);

        return Response.status(internalError.getHttpCode()).type(MediaType.APPLICATION_JSON).entity(internalError)
                .build();
    }

    public Response buildRedirectURI(URI location) {
        return Response.seeOther(location).build();
    }

}
