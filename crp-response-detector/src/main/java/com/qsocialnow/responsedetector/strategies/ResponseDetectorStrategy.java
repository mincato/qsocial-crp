package com.qsocialnow.responsedetector.strategies;

import java.util.List;

import com.qsocialnow.common.model.event.InPutBeanDocument;

public interface ResponseDetectorStrategy {

    List<InPutBeanDocument> findEvents();

}
