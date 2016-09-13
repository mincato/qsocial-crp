package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.Date;

public class RegistryListView implements Serializable {

    private static final long serialVersionUID = 4923657399304973277L;

    private String id;

    private String user;

    private String action;

    private String description;

    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
