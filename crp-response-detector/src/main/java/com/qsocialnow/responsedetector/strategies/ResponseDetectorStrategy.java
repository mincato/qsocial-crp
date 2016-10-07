package com.qsocialnow.responsedetector.strategies;

import java.util.List;

import com.qsocialnow.common.model.event.Event;

public interface ResponseDetectorStrategy {

    List<Event> findEvents();

}
