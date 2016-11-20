package com.qsocialnow.common.model.cases;

public enum Priority {

    HIGH(3), MEDIUM(2), LOW(1);

    private int sortOrder;

    private Priority(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
