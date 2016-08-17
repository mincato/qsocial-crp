package com.qsocialnow.rest.requestinfo;

import java.util.Date;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.rest.security.SecurityHandler;

@Service
@Priority(Priorities.USER + 10)
public class ResponseInfoPicker implements ContainerResponseFilter {

    @Context
    private HttpServletRequest request;

    @Autowired
    private SecurityHandler requestHandler;

    @Override
    public void filter(ContainerRequestContext inContext, ContainerResponseContext outContext) {

        RequestInfo requestInfo = requestHandler.getRequestInfoOrCreateNew(request);
        requestInfo.setResponseStatus(outContext.getStatus());
        requestInfo.setResponseEntity(outContext.getEntity());

        requestInfo.setEnd(new Date());
        requestInfo.setDuration(calculateDuration(requestInfo));

        requestHandler.saveRequestInfo(request, requestInfo);
    }

    private Long calculateDuration(final RequestInfo requestInfo) {
        Date start = requestInfo.getStart();
        Date end = requestInfo.getEnd();
        if (start == null || end == null) {
            return null;
        }
        return end.getTime() - start.getTime();
    }

}
