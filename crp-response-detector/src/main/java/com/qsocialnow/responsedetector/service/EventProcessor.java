package com.qsocialnow.responsedetector.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.kafka.producer.Producer;

@Component
public class EventProcessor {

    @Autowired
    private Producer kafkaProducer;

    public void process(List<Event> events) {
        if (CollectionUtils.isNotEmpty(events)) {
            Gson gson = new GsonBuilder().create();
            List<String> messages = events.stream().map(event -> gson.toJson(event)).collect(Collectors.toList());
            kafkaProducer.send(messages);
        }

    }

    public void process(Event event) {
        Gson gson = new GsonBuilder().create();
        String message = gson.toJson(event);
        kafkaProducer.send(message);
    }
}
