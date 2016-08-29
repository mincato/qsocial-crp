package com.qsocialnow.kafka.model;

public class Message {

    private String message;

    private String group;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMessage() {
        return message;
    }

    public String getGroup() {
        return group;
    }

}
