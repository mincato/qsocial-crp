package com.qsocialnow.retroactiveprocess.service;

import com.odatech.microservices.request.RealTimeReportBean;
import com.odatech.microservices.resolver.services.EventsPaginatedService;
import com.odatech.microservices.response.EventsPaginatedResponse;

public class EventsRetroactiveServiceImpl implements EventsRetroactiveService {

    private final EventsPaginatedService eventsPaginatedService;

    public EventsRetroactiveServiceImpl(EventsPaginatedService eventsPaginatedService) {
        this.eventsPaginatedService = eventsPaginatedService;
    }

    @Override
    public EventsPaginatedResponse buildResponse(RealTimeReportBean request) throws Throwable {
        return this.eventsPaginatedService.buildResponse(request);
    }

}
