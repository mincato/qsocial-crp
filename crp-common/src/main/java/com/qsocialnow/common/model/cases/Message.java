package com.qsocialnow.common.model.cases;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 6310291104125764822L;

    private String id;

    private boolean fromResponseDetector;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFromResponseDetector() {
        return fromResponseDetector;
    }

    public void setFromResponseDetector(boolean fromResponseDetector) {
        this.fromResponseDetector = fromResponseDetector;
    }

}
