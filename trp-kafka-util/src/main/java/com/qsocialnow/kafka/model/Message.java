package com.qsocialnow.kafka.model;

public class Message {

    private String message;

    private String topic;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public String getTopic() {
        return topic;
    }

}
