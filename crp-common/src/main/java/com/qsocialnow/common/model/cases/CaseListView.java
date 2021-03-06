package com.qsocialnow.common.model.cases;

import java.io.Serializable;

public class CaseListView implements Serializable {

    private static final long serialVersionUID = 6036361785847616703L;

    private String id;

    private String title;

    private String description;

    private String subject;

    private Long openDate;

    private boolean pendingResponse;

    private boolean open;

    private String priority;

    private String assignee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Long openDate) {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isPendingResponse() {
        return pendingResponse;
    }

    public void setPendingResponse(boolean pendingResponse) {
        this.pendingResponse = pendingResponse;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
