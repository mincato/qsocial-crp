package com.qsocialnow.retroactiveprocess.builder;

import com.odatech.microservices.request.RealTimeReportBean;
import com.qsocialnow.retroactiveprocess.service.EventsRetroactiveService;
import com.qsocialnow.retroactiveprocess.service.MockEventsRetroactiveService;

public class MockEventsRetroactiveServiceBuilder implements EventsRetroactiveServiceBuilder {

    @Override
    public EventsRetroactiveService build(RealTimeReportBean request) {
        return new MockEventsRetroactiveService();
    }

}
