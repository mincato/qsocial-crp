package com.qsocialnow.rest.response;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestExceptionMapper implements ExceptionMapper<Exception> {

    @Autowired
    private RestResponseHandler responseHandler;

    @Override
    public Response toResponse(Exception exception) {
        return responseHandler.buildErrorResponse(exception);
    }

}
