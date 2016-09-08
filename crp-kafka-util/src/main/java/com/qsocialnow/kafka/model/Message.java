package com.qsocialnow.kafka.model;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = -968583437662583196L;

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
