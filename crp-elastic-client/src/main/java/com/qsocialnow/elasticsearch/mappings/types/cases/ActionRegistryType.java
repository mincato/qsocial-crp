package com.qsocialnow.elasticsearch.mappings.types.cases;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.event.Event;

import io.searchbox.annotations.JestId;

public class ActionRegistryType implements IdentityType {

    @JestId
    private String id;

    private String idCase;

    private Boolean automatic;

    private String comment;

    private String userName;

    private Event event;

    private String action;

    private Long date;

    private ActionType type;

    private String deepLink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCase() {
        return idCase;
    }

    public void setIdCase(String idCase) {
        this.idCase = idCase;
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

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }
}
