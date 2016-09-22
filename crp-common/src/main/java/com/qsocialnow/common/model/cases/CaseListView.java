package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.Date;

public class CaseListView implements Serializable {

    private static final long serialVersionUID = 4923657399304973277L;

    private String id;

    private String title;

    private String description;

    private Date openDate;

    private boolean pendingResponse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPendingResponse() {
        return pendingResponse;
    }

    public void setPendingResponse(boolean pendingResponse) {
        this.pendingResponse = pendingResponse;
    }

}
