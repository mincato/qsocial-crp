package com.qsocialnow.retroactiveprocess.builder;

import com.odatech.microservices.request.RealTimeReportBean;
import com.qsocialnow.retroactiveprocess.service.EventsRetroactiveService;

public interface EventsRetroactiveServiceBuilder {

	EventsRetroactiveService build(RealTimeReportBean request);

}
