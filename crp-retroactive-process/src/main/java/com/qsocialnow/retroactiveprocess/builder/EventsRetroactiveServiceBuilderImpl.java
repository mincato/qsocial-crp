package com.qsocialnow.retroactiveprocess.builder;

import org.springframework.beans.factory.annotation.Autowired;

import com.odatech.microservices.request.RealTimeReportBean;
import com.odatech.microservices.resolver.MicroServiceBuilder;
import com.qsocialnow.retroactiveprocess.service.EventsPagedConfigService;
import com.qsocialnow.retroactiveprocess.service.EventsRetroactiveService;
import com.qsocialnow.retroactiveprocess.service.EventsRetroactiveServiceImpl;

public class EventsRetroactiveServiceBuilderImpl implements EventsRetroactiveServiceBuilder {

    @Autowired
    private EventsPagedConfigService eventsPagedConfigService;

    @Override
    public EventsRetroactiveService build(RealTimeReportBean request) {
        return new EventsRetroactiveServiceImpl(MicroServiceBuilder.buildPagedService(request.getTokenId(),
                eventsPagedConfigService.findRealtimeStaticServerByThematic(request.getTokenId())));
    }

}
