package com.qsocialnow.responsedetector.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.kafka.producer.Producer;

@Component
public class EventProcessor {

    private static final String EVENT_DATE_FORMAT = "dd/MM/yyyy";

    @Autowired
    private Producer kafkaProducer;

    public void process(List<InPutBeanDocument> events) {
        if (CollectionUtils.isNotEmpty(events)) {
            Gson gson = new GsonBuilder().setDateFormat(EVENT_DATE_FORMAT).create();
            List<String> messages = events.stream().map(event -> gson.toJson(event)).collect(Collectors.toList());
            kafkaProducer.send(messages);
        }

    }

    public void process(InPutBeanDocument event) {
        Gson gson = new GsonBuilder().setDateFormat(EVENT_DATE_FORMAT).create();
        String message = gson.toJson(event);
        kafkaProducer.send(message);
    }
}
