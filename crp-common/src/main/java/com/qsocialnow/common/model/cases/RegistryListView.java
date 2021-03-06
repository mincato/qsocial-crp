package com.qsocialnow.common.model.cases;

import java.io.Serializable;

public class RegistryListView implements Serializable {

    private static final long serialVersionUID = 9136979763891611520L;

    private String id;

    private String user;

    private String action;

    private String description;

    private Long date;

    private Boolean automatic;

    private String deepLink;

    private ActionRegistryStatus status;

    private ErrorType errorType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public ActionRegistryStatus getStatus() {
        return status;
    }

    public void setStatus(ActionRegistryStatus status) {
        this.status = status;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

}
