package com.qsocialnow.retroactiveprocess.builder;

import com.odatech.microservices.request.RealTimeReportBean;
import com.odatech.microservices.resolver.MicroServiceBuilder;
import com.qsocialnow.retroactiveprocess.service.EventsRetroactiveService;
import com.qsocialnow.retroactiveprocess.service.EventsRetroactiveServiceImpl;

public class EventsRetroactiveServiceBuilderImpl implements EventsRetroactiveServiceBuilder{

	@Override
	public EventsRetroactiveService build(RealTimeReportBean request) {
		return new EventsRetroactiveServiceImpl(MicroServiceBuilder.buildPagedService(request.getTokenId(),
		        "127.0.0.1"));
	}

}
