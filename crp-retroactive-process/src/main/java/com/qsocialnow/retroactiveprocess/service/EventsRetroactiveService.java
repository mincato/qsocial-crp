package com.qsocialnow.retroactiveprocess.service;

import com.odatech.microservices.request.RealTimeReportBean;
import com.odatech.microservices.response.EventsPaginatedResponse;

public interface EventsRetroactiveService {

    EventsPaginatedResponse buildResponse(RealTimeReportBean request) throws Throwable;

}
