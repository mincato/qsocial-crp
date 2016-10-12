package com.qsocialnow.eventresolver.processor;

import com.qsocialnow.kafka.model.Message;

public interface MessageProcessor {

    public void process(Message message) throws Exception;

}
