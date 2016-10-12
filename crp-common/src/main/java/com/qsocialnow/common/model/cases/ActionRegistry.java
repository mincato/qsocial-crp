package com.qsocialnow.common.model.cases;

import java.io.Serializable;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.event.Event;

public class ActionRegistry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Boolean automatic;

    private String comment;

    private String userName;

    private Event event;

    private String action;

    private Long date;

    private ActionType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }
}
