package com.qsocialnow.common.queues;

public enum QueueType {
    CASES("cases"), EVENTS("events");

    private final String type;

    QueueType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
