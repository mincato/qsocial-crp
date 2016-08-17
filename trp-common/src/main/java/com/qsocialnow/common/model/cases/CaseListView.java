package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.Date;

public class CaseListView implements Serializable {

    private static final long serialVersionUID = 4923657399304973277L;

    private String id;

    private String title;

    private Date openDate;

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

}
